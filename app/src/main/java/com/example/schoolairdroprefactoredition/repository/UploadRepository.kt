package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainUploadPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadToken
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.FileUtil
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.*
import com.qiniu.android.utils.LogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection

class UploadRepository private constructor() {

    companion object {

        /**
         * 键 上传额外参数
         */
        const val UPLOAD_PARAM_FILE_KEY = "file_key"

        /**
         * 任务id 上传额外参数
         */
        const val UPLOAD_PARAM_TASK_ID = "task_id"

        /**
         * 固定上传至临时文件夹
         */
        const val UPLOAD_PREFIX = "tmp"

        private var INSTANCE: UploadRepository? = null
        fun getInstance() = INSTANCE
                ?: UploadRepository().also {
                    INSTANCE = it
                }
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 上传图片
     *
     * 分三步进行，任何一步出问题都将返回null，成功后的结果将按照传进来的顺序返回所有图片的路径
     * 1、获取七牛云上传凭证 [getQiNiuToken]
     * 2、获取上传图片的服务器路径前缀和文件名 [requestForImagePath]
     * 3、上传图片 [uploadFileToQiNiu]
     *
     * @param token app用户验证token
     * @param fileLocalPaths 要上传的图片的本地路径
     * @param onResult 成功后按照传入的顺序返回图片路径，失败null
     */
    fun upload(token: String, fileLocalPaths: List<String>, type: String, onResult: (response: List<String>?) -> Unit) {
        // 将文件路径转换为本地文件
        val fileLocalList = ArrayList<File>(fileLocalPaths.size).apply {
            for (localPath in fileLocalPaths) {
                this.add(FileUtil.getFile(localPath))
            }
        }
        // 获取七牛云服务器上传凭证
        getQiNiuToken(token) { uploadToken ->
            if (uploadToken != null) {
                // 获取将要上传的图片在服务器上的路径前缀和文件名
                requestForImagePath(type, fileLocalPaths.size) { uploadPaths ->
                    if (uploadPaths != null) {
                        // 执行上传操作
                        uploadFileToQiNiu(fileLocalList, uploadPaths, uploadToken.data.token) {
                            if (it != null) {
                                onResult(it)
                            } else {
                                onResult(null)
                            }
                        }
                    } else {
                        onResult(null)
                    }
                }
            } else {
                onResult(null)
            }
        }
    }

    /**
     * 从服务器获取要上传的图片的路径前缀和图片名字
     *
     * @param type 要上传的图片的类型
     * @param amount 将要上传的图片的数量
     * @param onResult 成功返回图片路径前缀和n张图片的名字，失败null
     */
    private fun requestForImagePath(type: String, amount: Int, onResult: (response: DomainUploadPath?) -> Unit) {
        RetrofitClient.uploadApi.getImagePath(type, amount).apply {
            enqueue(object : CallBackWithRetry<DomainUploadPath>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainUploadPath>, response: Response<DomainUploadPath>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.code == ConstantUtil.HTTP_OK) {
                            onResult(body)
                        } else {
                            onResult(null)
                        }
                    } else {
                        LogUtils.d(response.errorBody()?.string())
                        onResult(null)
                    }
                }
            })
        }
    }

    /**
     * 获取七牛云上传凭证
     *
     * 首先从本地sp缓存中获取
     * 若本地缓存不存在或者已经过期，则从服务器重新获取
     *
     * @param token app用户验证token
     * @param onResult 成功返回七牛云上传凭证token，失败返回null
     */
    private fun getQiNiuToken(token: String, onResult: (response: DomainUploadToken?) -> Unit) {
        // 从本地sp获取
        val localUploadToken = jsonCacheUtil.getValue(DomainUploadToken.UPLOAD_TOKEN_KEY, DomainUploadToken::class.java)
        if (localUploadToken != null) {
            onResult(localUploadToken)
        } else {
            // 从服务器获取
            RetrofitClient.uploadApi.getUploadToken(token).apply {
                enqueue(object : CallBackWithRetry<DomainUploadToken>(this@apply) {
                    override fun onFailureAllRetries() {
                        onResult(null)
                    }

                    override fun onResponse(call: Call<DomainUploadToken>, response: Response<DomainUploadToken>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val body = response.body()
                            if (body != null && body.code == ConstantUtil.HTTP_OK) {
                                // 缓存新获取的upload token，有效至expire到期前3分钟
                                jsonCacheUtil.saveCache(DomainUploadToken.UPLOAD_TOKEN_KEY, body, body.data.expire * 1000 - 180_000)
                                onResult(body)
                            } else {
                                LogUtils.d(response.message())
                                onResult(null)
                            }
                        } else {
                            LogUtils.d(response.errorBody()?.string())
                            onResult(null)
                        }
                    }
                })
            }
        }
    }

    /**
     * 上传文件至七牛云
     *
     * 默认为多图模式，上传单图使用list包裹后传入即可
     *
     * @param fileLocalEntities 图片本地文件
     * @param fileKeysAndTaskID 图片将要放到服务器上的名字和任务id
     * @param uploadToken       图片上传所需要的七牛云凭证
     * @param onResult          上传成功返回按上传顺序排列的图片路径，失败返回null
     */
    private fun uploadFileToQiNiu(fileLocalEntities: List<File?>, fileKeysAndTaskID: DomainUploadPath, uploadToken: String, onResult: (response: List<String>?) -> Unit) {
        // 初始化上传管理类
        val uploadManager = UploadManager(Configuration
                .Builder()
                .useHttps(false)
                .zone(FixedZone.zone0).build())

        // 上传返回的结果集
        val responsePath = ArrayList<String>()
        // 要上传的文件在服务器的文件全名
        val fileFinalNames = ArrayList<String>()

        // 根据文件名、路径前缀和后缀将文件全名拼接出来
        val fileKeys = fileKeysAndTaskID.data.keys // keys 文件名
        for ((i, fileLocalEntity) in fileLocalEntities.withIndex()) {
            //获取文件后缀保证在服务器上的文件格式与本地一致，若无则默认为jpg
            val extension = if (fileLocalEntity?.extension.isNullOrEmpty()) ConstantUtil.JPEG else fileLocalEntity?.extension
            fileFinalNames.add((UPLOAD_PREFIX + fileKeys[i] + "." + extension))
        }

        // 上传的图片index
        var index = 0
        // rx同步顺序上传图片
        Observable
                .fromIterable(fileLocalEntities)
                .concatMap { fileLocalEntity ->
                    Observable.create(ObservableOnSubscribe<String> {
                        // 七牛云回调额外参数 设置图片的key和taskid
                        val uploadOptions = UploadOptions.defaultOptions()
                        uploadOptions.params[UPLOAD_PARAM_FILE_KEY] = fileKeys[index]
                        uploadOptions.params[UPLOAD_PARAM_TASK_ID] = fileKeysAndTaskID.data.taskId

                        // 同步上传，以确保上传顺序和list顺序一致
                        val response = uploadManager.syncPut(fileLocalEntity, fileFinalNames[index], uploadToken, null)
                        if (response.isOK) {
                            // 上传成功，发送图片的文件名至subscribe里的成功回调
                            it.onNext(fileFinalNames[index++])
                            it.onComplete()
                        } else {
                            // 上传失败，发送至subscribe中的失败回调，退出上传流程
                            it.onError(IOException(response.error))
                        }
                    }).subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 成功的回调将会回到这里
                    responsePath.add(it)
                    if (responsePath.size == fileLocalEntities.size) {
                        onResult(responsePath)
                        LogUtils.d("所有图片上传完毕，即将开始调用服务器接口")
                    }
                }) {
                    LogUtils.d(it.message)
                    onResult(null)
                }
    }
}
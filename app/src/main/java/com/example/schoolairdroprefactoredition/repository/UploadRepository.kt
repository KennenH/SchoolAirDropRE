package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIMPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadToken
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.FileUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.*
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
         * 文件在七牛云上的url，不带二级域名 上传额外参数
         */
        const val UPLOAD_PARAM_FILE_KEY = "x:file_key"

        /**
         * 本次上传任务分配到的id 上传额外参数
         */
        const val UPLOAD_PARAM_TASK_ID = "x:task_id"

        /**
         * 固定上传至临时文件夹
         *
         * 若本次任务因为某种原因被中断，则文件不会被转移出临时文件夹
         * 该文件夹下的所有文件存在24小时后自动删除
         */
        const val UPLOAD_PREFIX = "tmp/"

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
     * 分三步进行，成功后的结果将按照传进来的顺序返回所有图片的路径
     * 1、获取七牛云上传凭证 [getQiNiuToken]
     * 2、获取上传图片的服务器路径前缀和文件名 [requestForImagePath]
     * 3、上传图片 [uploadFileToQiNiu]
     *
     * @param token app用户验证token
     * @param fileLocalPaths 要上传的图片的本地路径
     * @param isNeedLarge 是否需要传大图，以true上传的图将会比false的大5倍左右
     * @param isSubscribeProgress 是否需要订阅上传进度
     * 当为true时外部的observer不能使用observeOnce，每当进入一个重要阶段都会通知外部
     * 当为false时只有在上传进度 完成 和 失败中断 两种情况才通知外部
     *
     * @param onResult 返回本次上传分配到的task id和所有被上传的图片被分配到的file keys
     * success 上一步操作是否成功 若为false则中断上传
     * tip 要求外部显示的提示，第一个Int为true则为res id，直接使用getString显示，否则使用 正在上传第%d张图片
     * taskAndKeys 请求的key和task id，在最后一步成功完成之前都是null，因此在最后一步之前都不能去判断这两个值
     */
    fun upload(
            token: String,
            fileLocalPaths: List<String>,
            type: String,
            isNeedLarge: Boolean = true,
            isSubscribeProgress: Boolean = false,
            onResult: (success: Boolean, tip: Pair<Int, Boolean>, taskAndKeys: DomainUploadPath.DataBean?, allSuccess: Boolean) -> Unit) {
        // 若发现传进来的图片数量是0则直接视为完成
        // 里边的onResult不要手贱去掉，否则外部收不到结束信号
        if (fileLocalPaths.isEmpty()) {
            onResult(true,
                    Pair(R.string.noNeedToHandleImages, true),
                    DomainUploadPath.DataBean().also {
                        it.taskId = ""
                        it.keys = ArrayList()
                    }, true)
            return
        }

        // 处理准备上传的文件
        if (isSubscribeProgress) onResult(true, Pair(R.string.handlingLocalMedia, true), null, false)
        // 将文件路径转换为本地文件
        val fileLocalList = ArrayList<File>(fileLocalPaths.size).apply {
            for (localPath in fileLocalPaths) {
                val localFile = FileUtil.compressFile(localPath, isNeedLarge)
                if (localFile != null) {
                    this.add(localFile)
                } else {
                    // 本地文件已经不存在时中断上传
                    onResult(false,
                            Pair(R.string.fileNotFound, false),
                            DomainUploadPath.DataBean().also {
                                it.taskId = ""
                                it.keys = ArrayList()
                            }, false)
                    return
                }
            }
        }
        if (isSubscribeProgress) onResult(true, Pair(R.string.requestingCredentials, true), null, false)
        // 获取七牛云服务器上传凭证
        getQiNiuToken(token) { uploadToken ->
            if (uploadToken != null) {
                if (isSubscribeProgress) onResult(true, Pair(R.string.resourceAllocating, true), null, false)
                // 获取将要上传的图片在服务器上的路径前缀和文件名
                requestForImagePath(token, type, fileLocalPaths.size) { taskAndKeysWrapper ->
                    if (taskAndKeysWrapper != null) {
                        // 执行上传操作
                        uploadFileToQiNiu(
                                fileLocalList,
                                taskAndKeysWrapper,
                                uploadToken.data.token) { success, toBeUpload, allSuccess ->
                            if (allSuccess) {
                                onResult(true, Pair(R.string.imagesUploadSuccess, true), taskAndKeysWrapper.data, true)
                            } else {
                                if (success) {
                                    if (isSubscribeProgress) onResult(true, Pair(toBeUpload, false), null, false)
                                } else {
                                    onResult(false, Pair(toBeUpload, false), null, false)
                                }
                            }
                        }
                    } else {
                        onResult(false, Pair(R.string.resourceAllocationFailed, true), null, false)
                    }
                }
            } else {
                onResult(false, Pair(R.string.inValidCredential, true), null, false)
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
    private fun requestForImagePath(token: String, type: String, amount: Int, onResult: (DomainUploadPath?) -> Unit) {
        RetrofitClient.uploadApi.getImagePath(token, type, amount).apply {
            enqueue(CallbackResultOrNull<DomainUploadPath>(this@apply, onResult))
        }
    }

    /**
     * 获取七牛云上传凭证
     *
     * 上传凭证有时效，时效内可以上传任意数量的图片，因此首先从本地sp缓存中获取
     * 若本地缓存不存在或者已经过期，则从服务器重新获取
     *
     * @param token app用户验证token
     * @param onResult 成功返回七牛云上传凭证token，失败返回null
     */
    private fun getQiNiuToken(token: String, onResult: (response: DomainUploadToken?) -> Unit) {
        // 从本地sp获取
        val localUploadToken = jsonCacheUtil.getCache(DomainUploadToken.UPLOAD_TOKEN_KEY, DomainUploadToken::class.java)
        if (localUploadToken != null) {
            onResult(localUploadToken)
        } else {
            // 从服务器获取
            RetrofitClient.uploadApi.getUploadToken(token).apply {
                enqueue(object : CallbackWithRetry<DomainUploadToken>(this@apply) {
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
     * @param fileKeysAndTaskID 图片将要放到服务器上的名字和本次上传任务分配到的id
     * @param uploadToken       图片上传所需要的七牛云凭证
     * @param onResult
     * success 上一张上传的图片是否成功，当此值为false时其他两值直接无视，中断上传
     * toBeUploaded 将要上传的图片，从1开始
     * allSuccess 全部上传都完成，当此值为true时代表上传成功
     */
    private fun uploadFileToQiNiu(
            fileLocalEntities: List<File?>,
            fileKeysAndTaskID: DomainUploadPath,
            uploadToken: String,
            onResult: (success: Boolean, toBeUploaded: Int, allSuccess: Boolean) -> Unit) {
        // 获取要上传的本地文件数量
        val size = fileLocalEntities.size

        // 初始化七牛上传管理类
        val uploadManager = UploadManager(Configuration
                .Builder()
                .useHttps(false)
                .zone(FixedZone.zone0).build())

        // 上传返回的结果集
        val responsePath = ArrayList<String>()
        // 要上传的文件在服务器的文件全名，下面hui
        val fileFinalNames = ArrayList<String>()

        // 根据文件名、路径前缀和后缀将文件全名拼接出来
        val fileKeys = fileKeysAndTaskID.data.keys // keys 文件名
        for (fileKey in fileKeys) {
            fileFinalNames.add(UPLOAD_PREFIX + fileKey)
        }

        // 上传的图片index
        var index = 0
        // rx同步顺序上传图片
        Observable
                .fromIterable(fileLocalEntities)
                .concatMap { fileLocalEntity ->
                    Observable.create(ObservableOnSubscribe<String> {
                        // 正在上传第index+1张图片
                        onResult(true, index + 1, false)

                        // 七牛云回调额外参数 设置图片的key和taskid
                        val uploadOptions = UploadOptions.defaultOptions()
                        uploadOptions.params[UPLOAD_PARAM_FILE_KEY] = fileKeys[index]
                        uploadOptions.params[UPLOAD_PARAM_TASK_ID] = fileKeysAndTaskID.data.taskId

                        // 同步上传，以确保上传顺序和list顺序一致
                        val putResult = uploadManager.syncPut(fileLocalEntity, fileFinalNames[index], uploadToken, uploadOptions)
                        if (putResult.isOK) {
                            // 上传成功，发送图片的文件名至subscribe里的成功回调
                            it.onNext(fileFinalNames[index])
                            it.onComplete()
                        } else {
                            // 上传失败，发送至subscribe中的失败回调，退出上传流程
                            it.onError(IOException(putResult.error))
                        }

                        // 下一张图片
                        index++
                    }).subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 成功的回调将会回到这里
                    responsePath.add(it)
                    if (responsePath.size == size) {
                        onResult(true, index, true)
                    }
                }) {
                    LogUtils.d(it.message)
                    onResult(false, index, false)
                }
    }

    /**
     * 移动在七牛云上的图片
     */
    fun moveIMImage(token: String, taskID: String, keys: String, onResult: (DomainIMPath?) -> Unit) {
        RetrofitClient.uploadApi.moveIMImages(token, taskID, keys).apply {
            enqueue(CallbackResultOrNull<DomainIMPath>(this@apply, onResult))
        }
    }
}
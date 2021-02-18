package com.example.schoolairdroprefactoredition.repository

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainUpload
import com.example.schoolairdroprefactoredition.domain.DomainUploadImage
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
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class UploadRepository private constructor() {

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.newInstance()
    }

    companion object {
        private var INSTANCE: UploadRepository? = null
        fun getInstance() = INSTANCE
                ?: UploadRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 上传图片
     *
     * 分三步进行，任何一步出问题都将返回null，成功将按照传进来的顺序返回所有图片的路径
     * 1、获取七牛云上传凭证
     * 2、获取上传图片的服务器路径前缀和文件名
     * 3、上传图片
     *
     * @param token app用户验证token
     * @param fileLocalPaths 要上传的图片的本地路径
     */
    fun doUpload(token: String, fileLocalPaths: List<String>, type: String, onResult: (response: List<String>?) -> Unit) {
        // 先获取七牛云服务器上传凭证
        getQiNiuToken(token) { uploadToken ->
            if (uploadToken != null) {
                // 然后获取将要上传的图片在服务器上的路径前缀和文件名
                requestForImagePath(type, fileLocalPaths.size) { uploadPaths ->
                    if (uploadPaths != null) {
                        // 最后再进行上传操作
                        uploadFile(fileLocalPaths, uploadPaths.data.keys, uploadToken.upload_token) {
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
                        LogUtil.d(response.errorBody()?.string())
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
                                // 缓存新获取的upload token，时效为27分钟
                                jsonCacheUtil.saveCache(DomainUploadToken.UPLOAD_TOKEN_KEY, body, 1620_000L)
                                onResult(body)
                            } else {
                                onResult(null)
                            }
                        } else {
                            LogUtil.d(response.errorBody()?.string())
                            onResult(null)
                        }
                    }
                })
            }
        }
    }

    /**
     * 上传文件
     *
     * 默认为多图模式，上传单图使用list包裹后传入即可
     *
     * @param fileLocalPaths 图片在本地的路径
     * @param fileNames      图片将要放到服务器上的路径和名字
     * @param uploadToken    图片上传所需要的七牛云凭证
     */
    private fun uploadFile(fileLocalPaths: List<String>, fileNames: List<String>, uploadToken: String, onResult: (response: List<String>?) -> Unit) {
        // 上传的图片index
        var index = 0
        // 上传返回的结果集
        val responsePath = ArrayList<String>()
        Observable
                .fromIterable(fileLocalPaths)
                .concatMap { filePath ->
                    Observable.create(ObservableOnSubscribe<String> {
                        val response = UploadManager(Configuration
                                .Builder()
                                .useHttps(false)
                                .zone(FixedZone.zone0).build())
                                .syncPut(filePath, fileNames[index], uploadToken, null)
                        if (response.isOK) {
                            // 上传成功，发送图片的文件名至subscribe
                            it.onNext(fileNames[index++])
                            it.onComplete()
                        } else {
                            it.onError(IOException(response.error))
                        }
                    }).subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 成功的回调将会回到这里
                    responsePath.add(it)
                    if (responsePath.size == fileLocalPaths.size) {
                        onResult(responsePath)
                    }
                }) {
                    LogUtil.d(it.message)
                    onResult(null)
                }
    }

    /**
     * 上传多图
     *
     * @param type
     * [ConstantUtil.UPLOAD_TYPE_AVATAR]
     * [ConstantUtil.UPLOAD_TYPE_GOODS]
     * [ConstantUtil.UPLOAD_TYPE_IM]
     * [ConstantUtil.UPLOAD_TYPE_POST]
     */
    @Deprecated("使用七牛云上传")
    fun uploadImage(context: Context,
                    imagesLocalPath: List<String>,
                    type: String,
                    onResult: (response: DomainUploadImage?) -> Unit) {
        val multipartList = ArrayList<MultipartBody.Part>(imagesLocalPath.size)
        val typePart = MultipartBody.Part.createFormData("type", type)

        for ((index, image) in imagesLocalPath.withIndex()) {
            val part = if (index == 0 && type == ConstantUtil.UPLOAD_TYPE_GOODS || type == ConstantUtil.UPLOAD_TYPE_POST) {
                FileUtil.createPartWithPath(context, ConstantUtil.KEY_UPLOAD_COVER, image, type != ConstantUtil.UPLOAD_TYPE_AVATAR)
            } else {
                FileUtil.createPartWithPath(context, "", image, type != ConstantUtil.UPLOAD_TYPE_AVATAR)
            }
            if (part != null) {
                multipartList.add(part)
            }
        }

        RetrofitClient.uploadApi.upload(multipartList, typePart).apply {
            enqueue(object : CallBackWithRetry<DomainUploadImage>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainUploadImage>, response: Response<DomainUploadImage>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body?.code == 200) {
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
     * 上传单图
     */
    @Deprecated("使用七牛云上传")
    fun uploadImage(
            context: Context,
            imageLocalPath: String,
            type: String,
            onResult: (response: DomainUpload?) -> Unit) {
        val imagePart = FileUtil.createPartWithPath(context, "file", imageLocalPath, type != ConstantUtil.UPLOAD_TYPE_AVATAR)
        val typePart = MultipartBody.Part.createFormData("type", type)
        if (imagePart != null) {
            RetrofitClient.uploadApi.upload(imagePart, typePart).apply {
                enqueue(object : CallBackWithRetry<DomainUpload>(this@apply) {
                    override fun onFailureAllRetries() {
                        onResult(null)
                    }

                    override fun onResponse(call: Call<DomainUpload>, response: Response<DomainUpload>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val body = response.body()
                            if (body != null && body.code == 200) {
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
        } else {
            onResult(null)
        }
    }
}
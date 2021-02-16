package com.example.schoolairdroprefactoredition.repository

import android.content.Context
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainUploadImage
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.FileUtil
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class UploadRepository private constructor() {

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
     * @param type
     * [ConstantUtil.UPLOAD_TYPE_AVATAR]
     * [ConstantUtil.UPLOAD_TYPE_GOODS]
     * [ConstantUtil.UPLOAD_TYPE_IM]
     * [ConstantUtil.UPLOAD_TYPE_POST]
     */
    fun uploadImage(context: Context,
                    imagesLocalPath: List<String>,
                    type: Int,
                    onResult: (response: DomainUploadImage?) -> Unit) {
        val multipartList = ArrayList<MultipartBody.Part>(imagesLocalPath.size)

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

        RetrofitClient.uploadApi.uploadImages(multipartList, type).apply {
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
                        onResult(null)
                    }
                }
            })
        }
    }
}
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

    fun uploadImage(context: Context,
                    images: List<String>,
                    type: Int,
                    onResult: (success: Boolean, response: DomainUploadImage?) -> Unit) {
        val multipartList = ArrayList<MultipartBody.Part>(images.size)
        for (image in images) {
            val part = if (images.indexOf(image) == 0 && type == ConstantUtil.UPLOAD_TYPE_GOODS || type == ConstantUtil.UPLOAD_TYPE_POST) {
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
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainUploadImage>, response: Response<DomainUploadImage>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body?.code == 200) {
                            onResult(true, body)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }

            })
        }
    }
}
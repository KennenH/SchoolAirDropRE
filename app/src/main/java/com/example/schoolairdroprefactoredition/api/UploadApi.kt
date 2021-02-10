package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainUploadImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.POST

/**
 * 上传图片单独api
 */
interface UploadApi {
    /**
     * 上传图片通用接口
     *
     * 若是上传物品或者上传帖子的cover的key为cover
     * ！！！！！！！！！！！！！！！！
     * ！！并且cover只能放在第一张！！
     * ！！！！！！！！！！！！！！！！
     */
    @POST("appapi/ajax/uploadImages")
    fun uploadImages(images: List<MultipartBody.Part>, type: Int): Call<DomainUploadImage>
}
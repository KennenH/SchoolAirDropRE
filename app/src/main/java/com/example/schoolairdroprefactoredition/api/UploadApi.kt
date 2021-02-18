package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainUpload
import com.example.schoolairdroprefactoredition.domain.DomainUploadImage
import com.example.schoolairdroprefactoredition.domain.DomainUploadPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadToken
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 上传图片单独api
 */
interface UploadApi {
    /**
     * 上传多图通用接口
     */
    @Deprecated("使用七牛云上传")
    @Multipart
    @POST("appapi/ajax/uploadImages")
    fun upload(@Part images: List<MultipartBody.Part>, @Part type: MultipartBody.Part): Call<DomainUploadImage>

    /**
     * 上传单图通用接口
     */
    @Deprecated("使用七牛云上传")
    @Multipart
    @POST("appapi/ajax/upload")
    fun upload(@Part image: MultipartBody.Part, @Part type: MultipartBody.Part): Call<DomainUpload>

    /**
     * 获取七牛云上传凭证
     */
    @POST("")
    fun getUploadToken(@Header("Authorization") token: String): Call<DomainUploadToken>

    /**
     * 获取上传图片的路径前缀和文件名
     *
     * @param type 图片类型 one of below
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_AVATAR] 头像
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_GOODS] 物品
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_IM] 聊天
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_POST] 帖子
     * @param amount 需要上传的图片数量
     */
    @FormUrlEncoded
    @POST("appapi/ajax/queryforpath")
    fun getImagePath(@Field("img_type") type: String, @Field("amount") amount: Int): Call<DomainUploadPath>
}
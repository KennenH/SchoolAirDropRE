package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface InquiryApi {

    /**
     * 提交新求购
     */
    @FormUrlEncoded
    @POST("inquiry/addNewInquiry")
    fun submitInquiry(
            @Header("Authorization") token: String,
            @Field("inquiry_content") content: String,
            @Field("inquiry_images") images: String,
            @Field("inquiry_tag_id") tagID: Int,
            @Field("inquiry_longitude") longitude: Double,
            @Field("inquiry_latitude") latitude: Double): Call<DomainResult>

    /**
     * 修改求购信息
     */
    @FormUrlEncoded
    @POST("inquiry/modifyInquiry")
    fun modifyInquiry(
            @Header("Authorization") token: String,
            @Field("inquiry_") content: String,
            @Field("inquiry_images") imagesToAdd: String,
            @Field("inquiry_delete") imagesToDelete: String,
            @Field("inquiry_tag_id") tagID: Int,
            @Field("inquiry_longitude") longitude: Double,
            @Field("inquiry_latitude") latitude: Double): Call<DomainResult>
}
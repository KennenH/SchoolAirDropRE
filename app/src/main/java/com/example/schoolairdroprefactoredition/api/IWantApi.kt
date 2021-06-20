package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.DomainUserIWant
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface IWantApi {

    /**
     * 提交新求购
     */
    @FormUrlEncoded
    @POST("appapi/IWant/uploadIwant")
    fun submitIWant(
            @Header("Authorization") token: String,
            @Field("task_id") taskID: String,
            @Field("file_keys") images: String,
            @Field("tag_id") tagID: Int,
            @Field("iwant_color") cardColor: Int,
            @Field("iwant_content") content: String,
            @Field("iwant_longitude") longitude: Double,
            @Field("iwant_latitude") latitude: Double): Call<DomainResult>

    /**
     * 修改求购信息
     */
    @FormUrlEncoded
    @POST("appapi/IWant/updateIWantInfo")
    fun modifyIWant(
            @Header("Authorization") token: String,
            @Field("iwant_color") cardColor: Int,
            @Field("iwant_tag") tagID: Int,
            @Field("delete_photo") imagesToDelete: String,
            @Field("task_id") taskID: String,
            @Field("content_file_keys") fileKeys: String,
            @Field("iwant_content") content: String,
            @Field("iwant_longitude") longitude: Double,
            @Field("iwant_latitude") latitude: Double): Call<DomainResult>

    /**
     * 删除我的求购
     */
    @FormUrlEncoded
    @POST("appapi/IWant/deleteIwant")
    fun deleteIWant(
            @Header("Authorization") token: String,
            @Field("iwant_id") iwantID: String): Call<DomainResult>

    /**
     * 获取附近求购信息
     */
    @FormUrlEncoded
    @POST("appapi/IWant/getNearByIWant")
    fun getNearByIWant(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("longitude") longitude: Double,
            @Field("latitude") latitude: Double,
            @Field("page") page: Int): Call<DomainIWant>

    /**
     * 以标签筛选求购
     */
    @FormUrlEncoded
    @POST("appapi/IWant/searchIWantByTag")
    fun searchIWantByTag(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("tag") tagID: Int,
            @Field("page") page: Int,
            @Field("longitude") longitude: Double,
            @Field("latitude") latitude: Double): Call<DomainIWant>

    /**
     * 搜索求购
     */
    @FormUrlEncoded
    @POST("appapi/IWant/searchIWant")
    fun searchIWant(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("keyWords") keyword: String,
            @Field("page") page: Int,
            @Field("longitude") longitude: Double,
            @Field("latitude") latitude: Double, ): Call<DomainIWant>


    /**
     * 以user id获取求购信息
     */
    @FormUrlEncoded
    @POST("appapi/IWant/getIwantInfoById")
    fun getIWantByUserID(
            @Field("user_id") userID: Int,
            @Field("page") page: Int,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainUserIWant>

    /**
     * 获取求购标签
     */
    @FormUrlEncoded
    @POST("appapi/tags/searchTags")
    fun getIWantTags(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainIWantTags>
}
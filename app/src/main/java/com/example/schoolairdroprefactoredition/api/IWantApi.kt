package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.domain.DomainResult
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
    @POST("appapi/iwant/addNewIwant")
    fun submitIWant(
            @Header("Authorization") token: String,
            @Field("iwant_color") cardColor: Int,
            @Field("iwant_tag_id") tagID: Int,
            @Field("iwant_images") images: String,
            @Field("iwant_content") content: String,
            @Field("iwant_longitude") longitude: Double,
            @Field("iwant_latitude") latitude: Double): Call<DomainResult>

    /**
     * 修改求购信息
     */
    @FormUrlEncoded
    @POST("appapi/iwant/modifyIwant")
    fun modifyIWant(
            @Header("Authorization") token: String,
            @Field("iwant_color") cardColor: Int,
            @Field("iwant_tag_id") tagID: Int,
            @Field("iwant_delete") imagesToDelete: String,
            @Field("iwant_images") imagesToAdd: String,
            @Field("iwant_content") content: String,
            @Field("iwant_longitude") longitude: Double,
            @Field("iwant_latitude") latitude: Double): Call<DomainResult>

    /**
     * 删除我的求购
     */
    @FormUrlEncoded
    @POST("appapi/iwant/deleteIwant")
    fun deteleIwant(
            @Header("Authorization") token: String,
            @Field("iwant_id") iwantID: Int)

    /**
     * 获取附近求购信息
     */
    @FormUrlEncoded
    @POST("appapi/iwant/getIwant")
    fun getIWant(
            @Field("longitude") longitude: Double,
            @Field("latitude") latitude: Double,
            @Field("page") page: Int)

    /**
     * 以user id获取求购信息
     */
    @FormUrlEncoded
    @POST("appapi/iwant/getIwantById")
    fun getIWantByUserID(
            @Field("user_id") userID: Int)

    /**
     * 获取求购标签
     */
    @FormUrlEncoded
    @POST("appapi/tags/searchTags")
    fun getIWantTags(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String) : Call<DomainIWantTags>
}
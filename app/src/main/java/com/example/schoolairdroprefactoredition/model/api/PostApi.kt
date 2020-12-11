package com.example.schoolairdroprefactoredition.model.api

import com.example.schoolairdroprefactoredition.domain.DomainPostInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PostApi {
    /**
     * 获取附近帖子
     */
    @FormUrlEncoded
    @POST("post/getNearByPost")
    fun getPostInfo(@Field("client_id") clientID: String,
                    @Field("client_secret") clientSecret: String,
                    @Field("page") page: Int,
                    @Field("longitude") longitude: Double?,
                    @Field("latitude") latitude: Double?): Call<DomainPostInfo>


}
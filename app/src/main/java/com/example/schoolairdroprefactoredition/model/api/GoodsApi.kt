package com.example.schoolairdroprefactoredition.model.api

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo
import com.example.schoolairdroprefactoredition.domain.DomainTrashBin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface GoodsApi {
    /**
     * 获取附近在售的商品
     */
    @FormUrlEncoded
    @POST("goods/getNearByGoods")
    fun getGoodsInfo(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("page") page: Int,
            @Field("longitude") longitude: Double?,
            @Field("latitude") latitude: Double?): Call<DomainGoodsInfo>

    /**
     * 获取垃圾箱
     */
    @POST("")
    fun getTrashBin(@Header("Authorization") token: String): Call<DomainTrashBin>
}
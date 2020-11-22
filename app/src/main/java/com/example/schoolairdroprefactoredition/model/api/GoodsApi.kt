package com.example.schoolairdroprefactoredition.model.api

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo
import com.example.schoolairdroprefactoredition.domain.DomainResult
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
     * 获取已中断的交易记录
     */
    @POST("")
    fun getCorrupted(@Header("Authorization") token: String): Call<DomainTrashBin>

    /**
     * 获取已完成的交易记录
     */
    @POST("")
    fun getAccomplished(@Header("Authorization") token: String): Call<DomainTrashBin>

    /**
     * 发起一个报价请求
     *
     * @param goodsID    物品id
     * @param quotePrice 报价价格
     */
    @FormUrlEncoded
    @POST("quote/quoteRequest")
    fun quoteRequest(@Header("Authorization") token: String,
                     @Field("goods_id") goodsID: String,
                     @Field("quote_price") quotePrice: String): Call<DomainResult>

}
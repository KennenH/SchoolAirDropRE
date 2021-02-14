package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface GoodsApi {
    /**
     * 获取附近在售的商品
     */
    @FormUrlEncoded
    @POST("goods/getNearByGoods")
    fun getNearByGoods(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("page") page: Int,
            @Field("longitude") longitude: Double?,
            @Field("latitude") latitude: Double?): Call<DomainPurchasing>

    /**
     * 获取物品详细信息
     */
    @FormUrlEncoded
    @POST("goods/getGoodsDetailInfo")
    fun getGoodsDetail(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("goods_id") goodsID: Int
    ): Call<GoodsDetailInfo>

    /**
     * 上传新的物品
     *
     * @param token       token
     * @param cover       封面
     * @param picSet      图片集
     * @param title       标题
     * @param price       价格
     * @param longitude   经度
     * @param latitude    纬度
     * @param negotiable  是否可议价
     * @param brandNew    是否全新
     * @param description 物品描述
     */
    @Multipart
    @POST("goods/upload")
    fun postNewItem(@Header("Authorization") token: String,
                    @Part cover: MultipartBody.Part,
                    @Part picSet: MultipartBody.Part,
                    @Part title: MultipartBody.Part,
                    @Part description: MultipartBody.Part,
                    @Part longitude: MultipartBody.Part,
                    @Part latitude: MultipartBody.Part,
                    @Part negotiable: MultipartBody.Part,
                    @Part brandNew: MultipartBody.Part,
                    @Part price: MultipartBody.Part): Call<DomainResult>

    /**
     * 获取用户个人在售
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSaleByClient")
    fun getGoodsOnSaleByClient(
            @Field("user_id") userID: Int,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainGoodsInfo>
}
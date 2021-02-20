package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface GoodsApi {

    /**
     * 下架我的在售物品
     */
    @FormUrlEncoded
    @POST("appapi/goods/deleteGoods")
    fun deleteGoods(@Header("Authorization") token: String,
                    @Field("goods_id") goodsID: Int): Call<DomainResult>

    /**
     * 搜索关键字物品
     */
    @FormUrlEncoded
    @POST("appapi/goods/searchGoods")
    fun searchGoods(@Field("client_id") clientID: String,
                    @Field("client_secret") clientSecret: String,
                    @Field("page") page: Int,
                    @Field("longitude") longitude: Double,
                    @Field("latitude") latitude: Double,
                    @Field("keyWords") keyWord: String): Call<DomainPurchasing>

    /**
     * 获取附近在售的商品
     */
    @FormUrlEncoded
    @POST("appapi/goods/getNearByGoods")
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
    @POST("appapi/goods/getGoodsDetailInfo")
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
     * @param goodsType   物品类型
     * @param description 物品描述
     */
    @Multipart
    @POST("appapi/goods/upload")
    fun postNewItem(@Header("Authorization") token: String,
                    @Part cover: MultipartBody.Part,
                    @Part picSet: MultipartBody.Part,
                    @Part title: MultipartBody.Part,
                    @Part description: MultipartBody.Part,
                    @Part longitude: MultipartBody.Part,
                    @Part latitude: MultipartBody.Part,
                    @Part goodsType: MultipartBody.Part,
                    @Part price: MultipartBody.Part): Call<DomainResult>

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
     * @param goodsType   物品类型
     * @param description 物品描述
     */
    @FormUrlEncoded
    @POST("appapi/goods/uploadGoods")
    fun postNewItem(@Header("Authorization") token: String,
                    @Field("goods_cover_image") cover: String,
                    @Field("goods_images") picSet: String,
                    @Field("goods_name") title: String,
                    @Field("goods_content") description: String,
                    @Field("goods_longitude") longitude: String,
                    @Field("goods_latitude") latitude: String,
                    @Field("goods_type") goodsType: String,
                    @Field("goods_price") price: String): Call<DomainResult>

    /**
     * 获取用户个人在售
     */
    @FormUrlEncoded
    @POST("appapi/goods/getGoodsOnSaleByClient")
    fun getGoodsOnSaleByClient(
            @Field("user_id") userID: Int,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainPurchasing>
}
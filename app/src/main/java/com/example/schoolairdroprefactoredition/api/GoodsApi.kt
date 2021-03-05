package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
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
            @Field("goods_id") goodsID: Int): Call<GoodsDetailInfo>

    /**
     * 获取物品全部详细信息
     */
    @FormUrlEncoded
    @POST("appapi/goods/getDetailInfo")
    fun getGoodsAllDetail(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("goods_id") goodsID: Int): Call<DomainGoodsAllDetailInfo>

    /**
     * 上传新的物品
     *
     * @param taskID      任务id
     * @param coverKey    封面的key
     * @param picSetKeys  图片集的keys，以逗号分隔的key字符串
     * @param token       token
     * @param title       标题
     * @param price       价格
     * @param longitude   经度
     * @param latitude    纬度
     * @param quotable    是否可议价
     * @param secondHand  是否是二手
     * @param description 物品描述
     */
    @FormUrlEncoded
    @POST("appapi/goods/uploadGoods")
    fun postNewItem(@Header("Authorization") token: String,
                    @Field("task_id") taskID: String,
                    @Field("cover_file_key") coverKey: String,
                    @Field("content_file_keys") picSetKeys: String,
                    @Field("goods_name") title: String,
                    @Field("goods_content") description: String,
                    @Field("goods_longitude") longitude: String,
                    @Field("goods_latitude") latitude: String,
                    @Field("goods_is_quotable") quotable: Int,
                    @Field("goods_is_secondHand") secondHand: Int,
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

    /**
     * 获取收藏物品详情
     */
    @FormUrlEncoded
    @POST("appapi/goods/")
    fun getFavoriteGoodsDetail(
            @Field("goods_ids") goodsIDs: String)
}
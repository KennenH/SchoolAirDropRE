package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.*
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
     * 获取附近的商品
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
    fun postNewItem(
            @Header("Authorization") token: String,
            @Field("task_id") taskID: String,
            @Field("cover_file_key") coverKey: String,
            @Field("content_file_keys") picSetKeys: String,
            @Field("goods_name") title: String,
            @Field("goods_content") description: String,
            @Field("goods_longitude") longitude: String,
            @Field("goods_latitude") latitude: String,
            @Field("goods_is_quotable") quotable: Int,
            @Field("goods_is_secondHand") secondHand: Int,
            @Field("goods_price") price: String
    ): Call<DomainResult>

    /**
     * 修改物品信息
     *
     * 修改物品信息不允许修改封面图片
     *
     * @param imagesToDelete 需要删除哪些图片
     * @param picSetKeys 新增的图片key
     * 当用户没有新增或者删除任何图片的时候这两个参数都为空即可
     *
     * 其余参数同[postNewItem]
     */
    @FormUrlEncoded
    @POST("appapi/goods/updateGoodsInfo")
    fun modifyGoodsInfo(
            @Header("Authorization") token: String,
            @Field("task_id") taskID: String,
            @Field("delete_photo") imagesToDelete: String,
            @Field("content_file_keys") picSetKeys: String,
            @Field("goods_id") goodsID: Int,
            @Field("goods_name") title: String,
            @Field("goods_content") description: String,
            @Field("goods_longitude") longitude: Double,
            @Field("goods_latitude") latitude: Double,
            @Field("goods_is_quotable") quotable: Int,
            @Field("goods_is_secondHand") secondHand: Int,
            @Field("goods_price") price: String
    ): Call<DomainResult>

    /**
     * 获取用户个人在售
     */
    @FormUrlEncoded
    @POST("appapi/goods/getGoodsOnSaleByClient")
    fun getGoodsOnSaleByClient(
            @Field("user_id") userID: Int,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainSelling>

    /**
     * 物品浏览量加一
     */
    @FormUrlEncoded
    @POST("appapi/goods/updateWatchCount")
    fun browseGoods(
            @Field("goods_id") goodsID: Int,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainResult>

    /**
     * 收藏
     */
    @FormUrlEncoded
    @POST("appapi/goods/updateFavorCount")
    fun favorGoods(
            @Field("Authorization") token: String,
            @Field("goods_id_string") goodsID: Int): Call<DomainResult>

    /**
     * 取消收藏
     */
    @FormUrlEncoded
    @POST("appapi/goods/deleteFavorCount")
    fun unFavorGoods(
            @Field("Authorization") token: String,
            @Field("goods_id_string") goodsID: Int): Call<DomainResult>
}
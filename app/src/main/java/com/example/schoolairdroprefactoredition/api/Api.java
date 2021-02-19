package com.example.schoolairdroprefactoredition.api;

import com.example.schoolairdroprefactoredition.domain.DomainPurchasing;
import com.example.schoolairdroprefactoredition.domain.DomainResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 网络数据接口类
 */
public interface Api {

    /**
     * 使用用户id获取用户在售信息
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSaleByClient")
    Call<DomainPurchasing> getUserSellingByID(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("uid") int userID);

    /**
     * 使用token获取本人在售物品列表
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSale")
    Call<DomainPurchasing> getMySellingGoods(@Field("user_id") int uid);

    /**
     * 下架物品
     */
    @FormUrlEncoded
    @POST("goods/deleteGoods")
    Call<DomainResult> unListItem(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 搜索关键字物品
     */
    @FormUrlEncoded
    @POST("goods/searchGoods")
    Call<DomainPurchasing> searchGoods(@Field("client_id") String clientID,
                                       @Field("client_secret") String clientSecret,
                                       @Field("page") int page,
                                       @Field("longitude") Double longitude,
                                       @Field("latitude") Double latitude,
                                       @Field("keyWords") String keyWord);

}

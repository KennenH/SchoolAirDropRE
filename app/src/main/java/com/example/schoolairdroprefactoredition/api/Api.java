package com.example.schoolairdroprefactoredition.api;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 网络数据接口类
 */
public interface Api {

    /**
     * 获取附近在售的商品
     */
    @FormUrlEncoded
    @POST("goods/getNearByGoods")
    Call<HomeGoodsListInfo> getGoodsInfo(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("page") int page, @Field("longitude") Double longitude, @Field("latitude") Double latitude);

    /**
     * 使用用户id获取用户在售信息
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSaleByClient")
    Call<HomeGoodsListInfo> getUserSellingByID(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("uid") int userID);

    /**
     * 使用token获取本人在售物品列表
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSale")
    Call<HomeGoodsListInfo> getMySellingGoods(@Field("user_id") int uid);

    /**
     * 使用GoodID获取物品详细信息
     */
    @FormUrlEncoded
    @POST("goods/getGoodsDetail")
    Call<DomainGoodsInfo> getGoodsDetailInfoByGoodsID(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("goodsID") int goodsID);

    /**
     * 下架物品
     */
    @FormUrlEncoded
    @POST("goods/deleteGoods")
    Call<DomainResult> unListItem(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 取消收藏物品
     */
    @FormUrlEncoded
    @POST("user/deleteFavor")
    Call<DomainResult> unFavoriteItem(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 判断该物品是否被收藏
     */
    @FormUrlEncoded
    @POST("user/isInFavor")
    Call<DomainResult> isItemFavored(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 获取用户收藏的物品
     */
    @POST("user/getUserFavor")
    Call<DomainGoodsInfo> getFavorites(@Header("Authorization") String token);

    /**
     * 上传新的物品
     */
    @POST("goods/upload")
    Call<DomainResult> postNewItem(@Body MultipartBody body);

    /**
     * 修改物品信息
     */
    @POST("goods/updateGoodsInfo")
    Call<DomainResult> updateItemInfo(@Body MultipartBody body);

    /**
     * 收藏物品
     *
     * @param goodsID 物品id
     */
    @FormUrlEncoded
    @POST("user/updateFavor")
    Call<DomainResult> favorite(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 搜索关键字物品
     */
    @FormUrlEncoded
    @POST("goods/searchGoods")
    Call<HomeGoodsListInfo> searchGoods(@Field("client_id") String clientID,
                                        @Field("client_secret") String clientSecret,
                                        @Field("page") int page,
                                        @Field("longitude") Double longitude,
                                        @Field("latitude") Double latitude,
                                        @Field("keyWords") String keyWord);

    /**
     * 上传用户头像
     */
    @POST("user/uploadAvatar")
    Call<DomainAvatarUpdateResult> updateAvatar(@Part MultipartBody.Part body);

    /**
     * 修改用户名字
     */
    @FormUrlEncoded
    @POST("user/updateUserInfo")
    Call<DomainResult> updateUserName(@Header("Authorization") String token, @Field("uname") String name, @Field("uid") int uid);

    /**
     * 用access_token换取用户基本信息
     */
    @FormUrlEncoded
    @POST("user/getUserInfo")
    Call<DomainUserInfo> getUserInfo(@Field("uid") int uid);

    /**
     * 服务器授权
     */
    @POST("authorize/login")
    Call<DomainAuthorizeGet> getAuthorizePublicKey();

    @FormUrlEncoded
    @POST("authorize/login")
    Call<DomainToken> authorize(@Header("Cookie") String sessionID
            , @Field("grant_type") String grantType
            , @Field("client_id") String clientID
            , @Field("client_secret") String clientSecret
            , @Field("alipay_id") String encryptedAlipayID
            , @Field("registration_id") String registrationID);
}

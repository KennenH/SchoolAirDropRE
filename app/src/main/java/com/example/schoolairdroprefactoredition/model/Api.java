package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
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
    Call<DomainGoodsInfo> getGoodsInfo(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("page") int page, @Field("longitude") Double longitude, @Field("latitude") Double latitude);

//    @FormUrlEncoded
//    @POST("paster/getGroundPaster")
//    Call<> getNearByNews(@Field("longitude") Double longitude, @Field("latitude") Double latitude);

    /**
     * 获取本人在售物品列表
     */
    @FormUrlEncoded
    @POST("goods/getGoodsOnSale")
    Call<DomainGoodsInfo> getMySellingGoods(@Header("Authorization") String token, @Field("page") int page);

    /**
     * 获取本人已售物品列表
     */
    @FormUrlEncoded
    @POST("goods/getUserSell")
    Call<DomainGoodsInfo> getMySoldGoods(@Header("Authorization") String token, @Field("page") int page);

    /**
     * 获取本人已购物品列表
     */
    @FormUrlEncoded
    @POST("goods/getUserBought")
    Call<DomainGoodsInfo> getMyBoughtGoods(@Header("Authorization") String token, @Field("page") int page);

    /**
     * 下架物品
     */
    @FormUrlEncoded
    @POST("goods/deleteGoods")
    Call<DomainResult> unListItem(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 收藏物品
     */
    @FormUrlEncoded
    @POST("goods/updateFavor")
    Call<DomainResult> favoriteItem(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 获取用户收藏的物品
     */
    @POST("goods/getUserFavor")
    Call<DomainGoodsInfo> getFavorites(@Header("Authorization") String token);

    /**
     * 发起一个报价请求
     *
     * @param goodsID    物品id
     * @param quotePrice 报价价格
     */
    @FormUrlEncoded
    @POST("goods/quoteRequest")
    Call<DomainResult> quoteRequest(@Header("Authorization") String token, @Field("goods_id") String goodsID, @Field("quote_price") String quotePrice);

    /**
     * 获取用户收到的以及发出的报价
     */
    @POST("goods/getUserQuote")
    Call<DomainGoodsInfo> getQuote(@Header("Authorization") String token);

    /**
     * 接受报价
     */
    @FormUrlEncoded
    @POST("goods/acceptQuote")
    Call<DomainResult> acceptQuote(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 拒绝报价
     */
    @FormUrlEncoded
    @POST("goods/refuseQuote")
    Call<DomainResult> refuseQuote(@Header("Authorization") String token, @Field("goods_id") String goodsID);

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
    Call<DomainResult> postNewItem(@Header("Authorization") String token,
                                   @Part MultipartBody.Part cover,
                                   @Part List<MultipartBody.Part> picSet,
                                   @Part MultipartBody.Part title,
                                   @Part MultipartBody.Part description,
                                   @Part MultipartBody.Part longitude,
                                   @Part MultipartBody.Part latitude,
                                   @Part MultipartBody.Part negotiable,
                                   @Part MultipartBody.Part brandNew,
                                   @Part MultipartBody.Part price);

    /**
     * 收藏物品
     *
     * @param goodsID 物品id
     */
    @FormUrlEncoded
    @POST("goods/updateFavor")
    Call<DomainResult> favorite(@Header("Authorization") String token, @Field("goods_id") String goodsID);

    /**
     * 搜索关键字物品
     */
    @FormUrlEncoded
    @POST("goods/searchGoods")
    Call<DomainGoodsInfo> searchGoods(@Field("client_id") String clientID,
                                      @Field("client_secret") String clientSecret,
                                      @Field("page") int page,
                                      @Field("longitude") Double longitude,
                                      @Field("latitude") Double latitude,
                                      @Field("keyWords") String keyWord);

    /**
     * 上传用户头像
     */
    @Multipart
    @POST("user/uploadAvatar")
    Call<DomainAvatarUpdateResult> updateAvatar(@Header("Authorization") String token, @Part MultipartBody.Part photo);

    /**
     * 修改用户名字
     */
    @FormUrlEncoded
    @POST("user/updateUserInfo")
    Call<DomainResult> updateUserName(@Header("Authorization") String token, @Field("uname") String name);

    /**
     * 修改用户性别
     */
    @FormUrlEncoded
    @POST("user/updateUserInfo")
    Call<DomainResult> updateUserSex(@Header("Authorization") String token, @Field("ugender") String gender);

    /**
     * 用access_token换取用户基本信息
     */
    @POST("user/getUserInfo")
    Call<DomainUserInfo> getUserInfo(@Header("Authorization") String token);

    /**
     * 获取首页最新消息
     */
    @GET
    Call<TestNewsItemBean> getNews();

    /**
     * 获取历史搜索
     */
    Call<DomainSearchItems> getSearchHistory();

    /**
     * 获取历史搜索的匹配和实时热词的建议
     */
    Call<DomainSearchItems> getSearchSuggestion();

    /**
     * 获取搜索商品结果
     */
    Call<TestGoodsItemBean> getSearchResult();


    /**
     * 服务器授权
     */
    @POST("authorize/login")
    Call<DomainAuthorizeGet> getAuthorizePublicKey();

    @FormUrlEncoded
    @POST("authorize/login")
    Call<DomainAuthorize> authorize(@Header("Cookie") String sessionID
            , @Field("grant_type") String grantType
            , @Field("client_id") String clientID
            , @Field("client_secret") String clientSecret
            , @Field("alipay_id") String encryptedAlipayID
            , @Field("registration_id") String registrationID);
}

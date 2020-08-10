package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizePost;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 网络数据接口类
 */
public interface Api {

    /**
     * 获取附近在售的商品
     * x-www-form-urlendoded
     */
    @FormUrlEncoded
    @POST("server/GetNearByGoods.php")
    Call<DomainGoodsInfo> getGoodsInfo(@Field("longitude") Double longitude, @Field("latitude") Double latitude);

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
     * 先空参请求公钥
     * 将alipayID使用公钥加密后post获取sessionID
     */
    @GET("server/authorize.php")
    Call<DomainAuthorizeGet> getAuthorizePublicKey();

    @POST("server/authorize.php")
    Call<DomainAuthorizePost> postAlipayIDRSA(@Query("user_alipay") String encryptedID);

}

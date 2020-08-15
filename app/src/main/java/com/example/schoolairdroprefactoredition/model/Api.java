package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdate;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
     * 表单
     */
    @FormUrlEncoded
    @POST("server/GetNearByGoods.php")
    Call<DomainGoodsInfo> getGoodsInfo(@Field("longitude") Double longitude, @Field("latitude") Double latitude);

    /**
     * 上传用户头像
     */
    @Multipart
    @POST("server/UpdateUserAvatar.php")
    Call<DomainAvatarUpdate> updateAvatar(@Part MultipartBody.Part photo, @Part("uid") RequestBody uid);



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
    @GET("server/authorize.php")
    Call<DomainAuthorizeGet> getAuthorizePublicKey();

    @FormUrlEncoded
    @POST("server/authorize.php")
    Call<DomainAuthorize> authorize(@Header("session_id") String sessionID
            , @Field("grant_type") String grantType
            , @Field("client_id") String clientID
            , @Field("client_secret") String clientSecret
            , @Field("user_alipay") String encryptedAlipayID);

}

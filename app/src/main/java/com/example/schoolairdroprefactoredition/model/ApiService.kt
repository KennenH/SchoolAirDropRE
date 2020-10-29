package com.example.schoolairdroprefactoredition.model

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    companion object {
        const val BASE_URL = "http://106.54.110.46:8000/"
    }

    /**
     * 获取公钥
     */
    @POST("authorize/login")
    fun getPublicKey(): Call<DomainAuthorizeGet>

    /**
     * 使用公钥加密的alipay id登录获取token
     */
    @FormUrlEncoded
    @POST("authorize/login")
    fun authorizeWithAlipayID(
            @Header("Cookie") sessionID: String,
            @Field("grant_type") grantType: String,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("alipay_id") encryptedAlipayID: String,
            @Field("registration_id") registrationID: String?): Call<DomainAuthorize>

    /**
     * 使用token获取用户信息
     */
    @FormUrlEncoded
    @POST("user/getUserInfo")
    fun getUserInfo(@Field("Authorization") token: String): Call<DomainUserInfo>


}
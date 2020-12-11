package com.example.schoolairdroprefactoredition.model.api

import com.example.schoolairdroprefactoredition.domain.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
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
            @Field("registration_id") registrationID: String?): Call<DomainToken>

    /**
     * 使用token获取用户信息
     */
    @POST("user/getUserInfo")
    fun getMyUserInfo(@Header("Authorization") token: String): Call<DomainUserInfo>

    /**
     * 使用用户id获取用户信息
     */
    @FormUrlEncoded
    @POST("/user/getUserInfoById")
    fun getUserInfoByID(
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("uid") userID: Int): Call<DomainBaseUser>
}
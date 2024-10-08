package com.example.schoolairdroprefactoredition.api

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
    @POST("appapi/auth/applogin")
    fun getPublicKey(): Call<DomainAuthorizeGet>

    /**
     * 使用公钥加密的alipay id登录获取token
     */
    @FormUrlEncoded
    @POST("appapi/auth/applogin")
    fun authorizeWithAlipayID(
            @Field("grant_type") grantType: String,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String,
            @Field("alipay_id") encryptedAlipayID: String,
            @Field("registration_id") registrationID: String?): Call<DomainToken>

    /**
     * 使用refresh token获取新的refresh token和access token
     */
    @FormUrlEncoded
    @POST("appapi/auth/refreshtoken")
    fun refreshToken(
            @Field("expire_access_token") accessToken: String,
            @Field("client_id") clientID: String,
            @Field("client_secret") clientSecret: String): Call<DomainToken>

    /**
     * 使用token获取用户信息
     */
    @POST("appapi/user/getInfo")
    fun getMyUserInfo(@Header("Authorization") token: String): Call<DomainUserInfo>

    /**
     * 使用用户id获取用户信息
     */
    @FormUrlEncoded
    @POST("appapi/user/getInfoByUid")
    fun getUserInfoByID(@Field("user_id") userID: Int): Call<DomainUserInfo>

    /**
     * 修改用户名字
     */
    @FormUrlEncoded
    @POST("appapi/user/updateUserInfo")
    fun updateUserName(@Header("Authorization") token: String, @Field("user_name") name: String): Call<DomainResult>

    /**
     * 修改用户性别
     */
    @FormUrlEncoded
    @POST("appapi/user/updateUserInfo")
    fun updateUserGender(@Header("Authorization") token: String, @Field("user_gender") gender: Boolean): Call<DomainResult>

    /**
     * 修改用户头像
     */
    @FormUrlEncoded
    @POST("appapi/user/updateAvatar")
    fun updateUserAvatar(@Header("Authorization") token: String, @Field("task_id") taskID: String, @Field("avatar") key: String): Call<DomainAvatarUpdateResult>

    /**
     * 校验token是否过期
     */
    @POST("appapi/auth/connect")
    fun verifyToken(@Header("Authorization") token: String): Call<DomainConnect>

    /**
     * 使用auth code换取用户alipay id
     * 使用alipay id可以为设备登录
     */
    @FormUrlEncoded
    @POST("appapi/auth/alipaycallback")
    fun getUserAlipayIDByAuthCode(@Field("auth_code") authCode: String): Call<DomainAlipayUserID>
}

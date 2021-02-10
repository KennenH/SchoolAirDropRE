package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    /**
     * 获取公钥
     *
     * http://81.69.14.64:8080/appapi/auth/login
     */
//    @POST("authorize/login")
    @POST("appapi/auth/login")
    fun getPublicKey(): Call<DomainAuthorizeGet>

    /**
     * 使用公钥加密的alipay id登录获取token
     *
     * http://81.69.14.64:8080/appapi/auth/login
     */
    @FormUrlEncoded
//    @POST("authorize/login")
    @POST("appapi/auth/login")
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
    @POST("appapi/user/getinfo")
    fun getMyUserInfo(@Header("Authorization") token: String): Call<DomainUserInfo>

    /**
     * 使用用户id获取用户信息
     */
    @FormUrlEncoded
    @POST("appapi/user/getInfoByUid")
    fun getUserInfoByID(@Field("uid") userID: Int): Call<DomainUserInfo>

    /**
     * 修改用户名字
     */
    @FormUrlEncoded
    @POST("appapi/user/updateUserName")
    fun updateUserName(@Header("Authorization") token: String, @Field("user_name") name: String): Call<DomainResult>


}
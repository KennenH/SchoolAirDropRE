package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.RSACoder
import com.mob.pushsdk.MobPush
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection


class LoginRepository private constructor() {

    companion object {
        private var INSTANCE: LoginRepository? = null
        fun getInstance() = INSTANCE
                ?: LoginRepository().also {
                    INSTANCE = it
                }
    }

    fun getPublicKey(onResult: (success: Boolean, response: DomainAuthorizeGet?) -> Unit) {
        RetrofitClient.userApi.getPublicKey().apply {
            enqueue(object : CallBackWithRetry<DomainAuthorizeGet>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainAuthorizeGet>, response: Response<DomainAuthorizeGet>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val result = response.body()
                        if (response.isSuccessful && result != null) {
                            val cookie = response.headers().get("Set-Cookie")
                            val session = cookie?.substring(0, cookie.indexOf(';'))
                            result.cookie = session
                            onResult(true, result)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }
            })
        }
    }

    fun authorizeWithAlipayID(
            cookies: String,
            rawAlipayID: String,
            publicKey: String,
            onResult: (success: Boolean, response: DomainToken?) -> Unit) {
        MobPush.getRegistrationId { registrationID ->
//            if (registrationID == null) {
//                onResult(false, null)
//            } else
            RetrofitClient.userApi.authorizeWithAlipayID(
                    cookies,
                    ConstantUtil.CLIENT_GRANT_TYPE,
                    ConstantUtil.CLIENT_ID,
                    ConstantUtil.CLIENT_SECRET,
                    RSACoder.encryptWithPublicKey(publicKey, rawAlipayID), registrationID)
                    .apply {
                        enqueue(object : CallBackWithRetry<DomainToken>(this@apply) {
                            override fun onResponse(call: Call<DomainToken>, response: Response<DomainToken>) {
                                if (response.code() == HttpURLConnection.HTTP_OK) {
                                    if (response.isSuccessful) {
                                        onResult(true, response.body())
                                    } else {
                                        onResult(false, null)
                                    }
                                } else {
                                    onResult(false, null)
                                }
                            }

                            override fun onFailureAllRetries() {
                                onResult(false, null)
                            }
                        })
                    }
        }
    }

    fun getUserInfo(token: String, onResult: (success: Boolean, response: DomainUserInfo?) -> Unit) {
        RetrofitClient.demoUserApi.getMyUserInfo(15).apply {
            enqueue(object : CallBackWithRetry<DomainUserInfo>(this@apply) {
                override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful) {
                            onResult(true, response.body())
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailureAllRetries() {
                    onResult(false, null)
                }
            })
        }
    }


}
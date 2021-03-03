package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
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

    /**
     * 每当app来到前台（不包括打开时）时调用token验证接口
     */
    fun connectWhenComesToForeground(token: String, onResult: () -> Unit) {
        RetrofitClient.userApi.verifyToken(token).apply {

        }
    }

    /**
     * 获取公钥
     */
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
//                            result.cookie = session
                            onResult(true, result)
                        } else {
                            onResult(false, null)
                        }
                    } else {
//                        LogUtils.d(response.errorBody()?.string())
                        onResult(false, null)
                    }
                }
            })
        }
    }

    /**
     * 通过alipayid登录
     */
    fun authorizeWithAlipayID(
            rawAlipayID: String,
            publicKey: String,
            onResult: (success: Boolean, response: DomainToken?) -> Unit) {
        MobPush.getRegistrationId { registrationID ->
//            if (registrationID == null) {
//                onResult(false, null)
//            } else
            RetrofitClient.userApi.authorizeWithAlipayID(
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

    /**
     * 获取我的用户信息
     */
    fun getMyInfo(token: String, onResult: (success: Boolean, response: DomainUserInfo.DataBean?) -> Unit) {
        RetrofitClient.userApi.getMyUserInfo(token).apply {
            enqueue(object : CallBackWithRetry<DomainUserInfo>(this@apply) {
                override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (response.isSuccessful) {
                            onResult(true, body?.data)
                        } else {
                            onResult(false, null)
                        }
                    } else {
//                        LogUtils.d(response.errorBody()?.string())
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
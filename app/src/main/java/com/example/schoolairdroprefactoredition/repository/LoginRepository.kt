package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.util.UserLoginCacheUtil
import com.example.schoolairdroprefactoredition.domain.*
import com.example.schoolairdroprefactoredition.utils.*
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
     * 使用支付宝auth code换取用户alipay id
     * 获取到的alipay id用于登录app
     */
    fun getAlipayIDByAuthCode(authCode: String, onResult: (String?) -> Unit) {
        RetrofitClient.userApi.getUserAlipayIDByAuthCode(authCode).apply {
            enqueue(object : CallbackWithRetry<DomainAlipayUserID>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainAlipayUserID>, response: Response<DomainAlipayUserID>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body?.code == ConstantUtil.HTTP_OK) {
                            body.data.alipay_id.let {
                                onResult(it)
                            }
                        } else {
                            onResult(null)
                        }
                    }
                }
            })
        }
    }

    /**
     * 每当app来到前台（不包括打开时）时调用token验证接口
     *
     * @param onResult 接收code
     * [ConstantUtil.HTTP_OK] 为验证通过
     * [ConstantUtil.HTTP_BAD_REQUEST] 为验证失败，token已失效
     * null 为其他错误
     */
    fun connectWhenComesToForeground(token: String, onResult: (code: Int?) -> Unit) {
        RetrofitClient.userApi.verifyToken(token).apply {
            enqueue(object : CallbackWithRetry<DomainConnect>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainConnect>, response: Response<DomainConnect>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        onResult(body?.code)
                    } else {
                        onResult(null)
                    }
                }
            })
        }
    }

    /**
     * 获取公钥
     */
    fun getPublicKey(onResult: (response: String?) -> Unit) {
        RetrofitClient.userApi.getPublicKey().apply {
            enqueue(object : CallbackWithRetry<DomainAuthorizeGet>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainAuthorizeGet>, response: Response<DomainAuthorizeGet>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val result = response.body()
                        if (response.isSuccessful && result != null) {
                            onResult(result.data.public_key)
                        } else {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                }
            })
        }
    }

    /**
     * 当前token已过期，使用上一个有效的token换取新的token
     */
    fun refreshToken(
            token: String,
            onResult: (response: DomainToken?, code: Int) -> Unit) {
        RetrofitClient.userApi.refreshToken(
                token.substring(7),
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET).apply {
            enqueue(object : CallbackWithRetry<DomainToken>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null, ConstantUtil.HTTP_BAD_REQUEST)
                }

                override fun onResponse(call: Call<DomainToken>, response: Response<DomainToken>) {
                    val body = response.body()
                    when (response.code()) {
                        // refresh token刷新成功，获取新的token
                        ConstantUtil.HTTP_OK -> {
                            onResult(body, ConstantUtil.HTTP_OK)
                            if (body != null) {
                                UserLoginCacheUtil.getInstance().saveUserToken(body)
                            }
                        }

                        // refresh token过期，需要用户重新登录
                        ConstantUtil.HTTP_INVALID_REFRESH_TOKEN -> {
                            onResult(null, ConstantUtil.HTTP_INVALID_REFRESH_TOKEN)
                        }

                        else -> {
                            onResult(null, response.code())
                        }
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
            onResult: (response: DomainToken?) -> Unit) {
        MobPush.getRegistrationId { registrationID ->
            val encryptWithPublicKey = RSACoder.encryptWithPublicKey(publicKey, rawAlipayID)
            if (encryptWithPublicKey != null) {
                RetrofitClient.userApi.authorizeWithAlipayID(
                        ConstantUtil.CLIENT_GRANT_TYPE,
                        ConstantUtil.CLIENT_ID,
                        ConstantUtil.CLIENT_SECRET,
                        encryptWithPublicKey,
                        registrationID)
                        .apply {
                            enqueue(object : CallbackWithRetry<DomainToken>(this@apply) {
                                override fun onResponse(call: Call<DomainToken>, response: Response<DomainToken>) {
                                    if (response.code() == HttpURLConnection.HTTP_OK) {
                                        val token = response.body()
                                        if (token != null) {
                                            UserLoginCacheUtil.getInstance().saveUserToken(token)
                                            onResult(UserLoginCacheUtil.getInstance().getUserToken())
                                        } else {
                                            onResult(null)
                                        }
                                    } else {
                                        LogUtils.d(response.errorBody()?.string())
                                        onResult(null)
                                    }
                                }

                                override fun onFailureAllRetries() {
                                    onResult(null)
                                }
                            })
                        }
            } else {
                onResult(null)
            }
        }
    }

    /**
     * 获取我的用户信息
     */
    fun getMyInfo(token: String, onResult: (response: DomainUserInfo.DataBean?) -> Unit) {
        RetrofitClient.userApi.getMyUserInfo(token)
                .apply {
                    enqueue(object : CallbackWithRetry<DomainUserInfo>(this@apply) {
                        override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                val body = response.body()
                                if (body?.data != null) {
                                    onResult(body.data)
                                    UserLoginCacheUtil.getInstance().saveUserInfo(body.data)
                                } else {
                                    onResult(null)
                                }
                            } else {
                                onResult(null)
                            }
                        }

                        override fun onFailureAllRetries() {
                            onResult(null)
                        }
                    })
                }
    }


}
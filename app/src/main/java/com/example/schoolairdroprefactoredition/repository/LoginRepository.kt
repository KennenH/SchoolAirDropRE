package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainAlipayUserID
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
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
            enqueue(object : CallBackWithRetry<DomainAlipayUserID>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainAlipayUserID>, response: Response<DomainAlipayUserID>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body?.code == ConstantUtil.HTTP_OK) {
                            body.data.alipay_id.let {
                                onResult(it)
                                // 永久保存用户alipay id
                                UserLoginCacheUtils.getInstance().saveUserAlipayID(it)
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
     */
    fun connectWhenComesToForeground(token: String, onResult: () -> Unit) {
        RetrofitClient.userApi.verifyToken(token).apply {

        }
    }

    /**
     * 获取公钥
     */
    fun getPublicKey(onResult: (response: String?) -> Unit) {
        RetrofitClient.userApi.getPublicKey().apply {
            enqueue(object : CallBackWithRetry<DomainAuthorizeGet>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainAuthorizeGet>, response: Response<DomainAuthorizeGet>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val result = response.body()
                        if (response.isSuccessful && result != null) {
//                            result.cookie = session
                            onResult(result.public_key)
                        } else {
                            onResult(null)
                        }
                    } else {
//                        LogUtils.d(response.errorBody()?.string())
                        onResult(null)
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
                                    val token = response.body()
                                    if (token != null) {
                                        onResult(token)
                                        UserLoginCacheUtils.getInstance().saveUserToken(token)
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
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.*
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheConstantUtil
import com.example.schoolairdroprefactoredition.cache.util.UserLoginCacheUtil
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.repository.LoginRepository
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.qiniu.android.utils.LogUtil
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginRepository by lazy {
        LoginRepository.getInstance()
    }

    /**
     * debug 登录
     */
    fun loginDebug(): LiveData<DomainToken?> {
        val token = MutableLiveData<DomainToken?>()
        // 获取alipay id之后获取公钥
        loginRepository.getPublicKey { publicKey ->
            if (publicKey != null) {
                // 获取公钥之后加密alipay id传输获取app token
                loginRepository.authorizeWithAlipayID(
                        AppConfig.USER_ALIPAY,
                        publicKey) {
                    token.postValue(it)
                }
            } else {
                token.postValue(null)
            }
        }
        return token
    }

    /**
     * 使用auth code换取用户alipay id，再使用alipay id登录app
     * 总共分为三步，其中任何一步出错将返回null
     */
    fun loginWithAlipayAuthCode(authCode: String?): LiveData<DomainToken?> {
        val token = MutableLiveData<DomainToken?>()
        viewModelScope.launch {
            if (authCode != null) {
                // 使用auth code获取alipay id
                loginRepository.getAlipayIDByAuthCode(authCode) { alipayID ->
                    if (alipayID != null) {
                        // 获取alipay id之后获取公钥
                        loginRepository.getPublicKey { publicKey ->
                            if (publicKey != null) {
                                // 获取公钥之后加密alipay id传输获取app token
                                loginRepository.authorizeWithAlipayID(
                                        alipayID,
                                        publicKey) {
                                    token.postValue(it)
                                }
                            } else {
                                token.postValue(null)
                            }
                        }
                    } else {
                        token.postValue(null)
                    }
                }
            } else {
                token.postValue(null)
            }
        }
        return token
    }

    /**
     * 用旧的refresh token换取新的refresh token
     */
    fun refreshToken(token: DomainToken): LiveData<Pair<DomainToken?, Int>> {
        val newToken = MutableLiveData<Pair<DomainToken?, Int>>()
        viewModelScope.launch {
            loginRepository.refreshToken(token.access_token) { token, code ->
                newToken.postValue(Pair(token, code))
            }
        }
        return newToken
    }

    /**
     * 使用token获取用户信息
     */
    fun getMyInfo(token: String): LiveData<DomainUserInfo.DataBean?> {
        val myInfo = MutableLiveData<DomainUserInfo.DataBean?>()
        viewModelScope.launch {
            loginRepository.getMyInfo(token) { response ->
                myInfo.postValue(response)
            }
        }
        return myInfo
    }

    /**
     * 用户登出，删除用户账号相关的本地缓存，包括用户alipay id、用户token、用户信息
     */
    fun logout() {
        // 删除用户alipay id
        UserLoginCacheUtil.getInstance().deleteCache(JsonCacheConstantUtil.USER_ALIPAY_ID)
        // 删除用户token
        UserLoginCacheUtil.getInstance().deleteCache(UserTokenCache.KEY)
        // 删除用户信息
        UserLoginCacheUtil.getInstance().deleteCache(UserInfoCache.KEY)
    }

    /**
     * app进入前台时检查token是否过期
     */
    fun connectWhenComesToForeground(token: String?): MutableLiveData<Int?> {
        val connect = MutableLiveData<Int?>()
        viewModelScope.launch {
            if (token != null) {
                loginRepository.connectWhenComesToForeground(token) {
                    connect.postValue(it)
                }
            }
        }
        return connect
    }
}
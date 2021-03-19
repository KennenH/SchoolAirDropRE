package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainAlipayUserID
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.repository.LoginRepository
import com.example.schoolairdroprefactoredition.utils.JsonCacheConstantUtil
import com.example.schoolairdroprefactoredition.utils.UserLoginCacheUtils
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginRepository by lazy {
        LoginRepository.getInstance()
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
     * 使用alipay id登录app
     */
    fun loginWithAlipayID(alipayID: String): LiveData<DomainToken?> {
        val token = MutableLiveData<DomainToken?>()
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
        return token
    }

    /**
     * 使用token获取用户信息
     */
    fun getMyInfo(token: String): LiveData<DomainUserInfo.DataBean?> {
        val myInfo = MutableLiveData<DomainUserInfo.DataBean?>()
        viewModelScope.launch {
            loginRepository.getMyInfo(token) { success, response ->
                if (success) {
                    myInfo.postValue(response)
                } else {
                    myInfo.postValue(null)
                }

                if (response != null) {
                    UserLoginCacheUtils.getInstance().saveUserInfo(response)
                }
            }
        }
        return myInfo
    }

    /**
     * 用户登出，删除用户账号相关的本地缓存，包括用户alipay id、用户token、用户信息
     */
    fun logout() {
        // 删除用户alipay id
        UserLoginCacheUtils.getInstance().deleteCache(JsonCacheConstantUtil.USER_ALIPAY_ID)
        // 删除用户token
        UserLoginCacheUtils.getInstance().deleteCache(UserTokenCache.KEY)
        // 删除用户信息
        UserLoginCacheUtils.getInstance().deleteCache(UserInfoCache.KEY)
    }

    /**
     * app进入前台时检查token是否过期
     */
    fun connectWhenComesToForeground(token: String) {
        viewModelScope.launch {
            loginRepository.connectWhenComesToForeground(token) {

            }
        }
    }
}
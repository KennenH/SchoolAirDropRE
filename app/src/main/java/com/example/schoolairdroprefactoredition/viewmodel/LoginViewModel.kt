package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.repository.LoginRepository
import com.example.schoolairdroprefactoredition.utils.UserLoginCacheUtils
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginRepository by lazy {
        LoginRepository.getInstance()
    }

    /**
     * 获取RSA公钥
     */
    fun getPublicKey(): LiveData<DomainAuthorizeGet?> {
        val mPublicKey = MutableLiveData<DomainAuthorizeGet?>()
        viewModelScope.launch {
            loginRepository.getPublicKey { success, response ->
                if (success) {
                    mPublicKey.postValue(response)
                } else {
                    mPublicKey.postValue(null)
                }
            }
        }
        return mPublicKey
    }

    /**
     * 使用alipay id登录
     */
    fun authorizeWithAlipayID(
            rawAlipayID: String,
            publicKey: String,
    ): LiveData<DomainToken?> {
        val mAuthorize = MutableLiveData<DomainToken?>()
        viewModelScope.launch {
            loginRepository.authorizeWithAlipayID(
                    rawAlipayID,
                    publicKey,
            ) { success, response ->
                if (success) {
                    mAuthorize.postValue(response)
                } else {
                    mAuthorize.postValue(null)
                }
                if (response != null) {
                    UserLoginCacheUtils.instance.saveUserToken(response)
                }
            }
        }
        return mAuthorize
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
                    UserLoginCacheUtils.instance.saveUserInfo(response)
                }
            }
        }
        return myInfo
    }

    /**
     * 登出
     */
    fun logout() {
        UserLoginCacheUtils.instance.deleteCache(UserTokenCache.KEY)
        UserLoginCacheUtils.instance.deleteCache(UserInfoCache.KEY)
    }

//    private var connectLiveData: MutableLiveData<DomainConnect> = MutableLiveData()

    /**
     * app进入前台时检查token是否过期
     */
    fun connectWhenComesToForeground(token: String) {
        viewModelScope.launch {
            loginRepository.connectWhenComesToForeground(token){

            }
        }
    }
}
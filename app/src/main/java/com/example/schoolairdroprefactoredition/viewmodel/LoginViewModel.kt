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
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.LoginRepository
import com.example.schoolairdroprefactoredition.utils.UserLoginCacheUtils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val mainRepository by lazy {
        LoginRepository.getInstance()
    }

    /**
     * 获取RSA公钥
     */
    fun getPublicKey(): LiveData<DomainAuthorizeGet?> {
        val mPublicKey = MutableLiveData<DomainAuthorizeGet?>()
        viewModelScope.launch {
            mainRepository.getPublicKey { success, response ->
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
            mainRepository.authorizeWithAlipayID(
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
    fun getUserInfo(token: String): LiveData<DomainUserInfo.DataBean?> {
        val mUserInfo = MutableLiveData<DomainUserInfo.DataBean?>()
        viewModelScope.launch {
            mainRepository.getUserInfo(token) { success, response ->
                if (success) {
                    mUserInfo.postValue(response)
                } else {
                    mUserInfo.postValue(null)
                }

                if (response != null) {
                    UserLoginCacheUtils.instance.saveUserInfo(response)
                }
            }
        }
        return mUserInfo
    }

    /**
     * 登出
     */
    fun logout() {
        UserLoginCacheUtils.instance.deleteCache(UserTokenCache.KEY)
        UserLoginCacheUtils.instance.deleteCache(UserInfoCache.KEY)
    }
}
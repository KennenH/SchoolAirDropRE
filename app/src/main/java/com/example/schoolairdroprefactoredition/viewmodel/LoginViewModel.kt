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
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loadState = MutableLiveData<LoadState>()

    private val mPublicKey = MutableLiveData<DomainAuthorizeGet>()
    private val mAuthorize = MutableLiveData<DomainToken>()
    private val mUserInfo = MutableLiveData<DomainUserInfo>()

    private val mainRepository by lazy {
        LoginRepository.getInstance()
    }

    fun getLoadState(): LiveData<LoadState> {
        return loadState
    }

    /**
     * 获取RSA公钥
     */
    fun getPublicKey(): LiveData<DomainAuthorizeGet> {
        viewModelScope.launch {
            loadState.value = LoadState.LOADING
            mainRepository.getPublicKey { success, response ->
                if (success) {
                    mPublicKey.postValue(response)
                } else {
                    loadState.value = LoadState.ERROR
                }
            }
        }
        return mPublicKey
    }

    /**
     * 使用alipay id登录
     */
    fun authorizeWithAlipayID(
            cookies: String,
            rawAlipayID: String,
            publicKey: String,
    ): LiveData<DomainToken> {
        viewModelScope.launch {
            mainRepository.authorizeWithAlipayID(
                    cookies,
                    rawAlipayID,
                    publicKey,
            ) { success, response ->
                if (success) {
                    mAuthorize.postValue(response)
                } else {
                    loadState.value = LoadState.ERROR
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
    fun getUserInfo(token: String): LiveData<DomainUserInfo> {
        viewModelScope.launch {
            mainRepository.getUserInfo(token) { success, response ->
                if (success) {
                    mUserInfo.postValue(response)
                    loadState.value = LoadState.SUCCESS
                } else {
                    loadState.value = LoadState.ERROR
                }

                if (response != null) {
                    UserLoginCacheUtils.instance.saveUserInfo(response.data[0])
                }
            }
        }
        return mUserInfo
    }

    /**
     * 登出
     */
    fun logout() {
        UserLoginCacheUtils.instance.deleteCache(UserTokenCache.USER_TOKEN)
        UserLoginCacheUtils.instance.deleteCache(UserInfoCache.USER_INFO)
    }


}
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import java.util.*

class AccountViewModel : ViewModel() {

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 获取上次登录的用户的基本信息
     */
    val lastLoggedUserInfoCache: DomainUserInfo.DataBean?
        get() {
            var userInfoCache = mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)
            if (userInfoCache == null) {
                userInfoCache = UserInfoCache()
            }
            return userInfoCache.getLastLoggedAccount()
        }

    /**
     * 获取上次登录用户的token信息
     */
    val lastLoggedTokenCaChe: DomainToken?
        get() {
            var userTokenCache = mJsonCacheUtil.getCache(UserTokenCache.KEY, UserTokenCache::class.java)
            if (userTokenCache == null) {
                userTokenCache = UserTokenCache()
            }
            return userTokenCache.token
        }

    /**
     * 获取本设备上保存的所有用户信息
     */
    val allUserCacheOnThisDevice: ArrayList<DomainUserInfo.DataBean>
        get() {
            var userInfoCache = mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)
            if (userInfoCache == null) {
                userInfoCache = UserInfoCache()
            }
            return userInfoCache.getAllUsersOnThisDevice()
        }
}
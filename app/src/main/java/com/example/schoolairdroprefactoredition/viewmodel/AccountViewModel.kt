package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.cache.util.UserLoginCacheUtil
import com.example.schoolairdroprefactoredition.utils.AppConfig
import java.util.*

class AccountViewModel : ViewModel() {

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
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
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.cache.JsonCacheUtil
import com.example.schoolairdroprefactoredition.cache.UserLoginCacheUtils
import com.example.schoolairdroprefactoredition.utils.AppConfig
import java.util.*

class AccountViewModel : ViewModel() {

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 获取上次登录用户的alipay id
     */
    val lastLoggedUserAlipayID: String? = if (AppConfig.IS_DEBUG) AppConfig.USER_ALIPAY else UserLoginCacheUtils.getInstance().getUserAlipayID()

    /**
     * 获取上次登录的用户的基本信息
     */
    val lastLoggedUserInfoCache: DomainUserInfo.DataBean? = UserLoginCacheUtils.getInstance().getUserInfo()

    /**
     * 获取上次登录用户的token信息
     */
    val lastLoggedTokenCaChe: DomainToken? = UserLoginCacheUtils.getInstance().getUserToken()


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
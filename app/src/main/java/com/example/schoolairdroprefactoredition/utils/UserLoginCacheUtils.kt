package com.example.schoolairdroprefactoredition.utils

import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo

class UserLoginCacheUtils {
    companion object {
        val instance = UserLoginCacheUtils()
        val mJsonCacheUtil: JsonCacheUtil = JsonCacheUtil.newInstance()
    }

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token    本次登录获取到的token
     * @param duration token持续时间
     */
    fun saveUserToken(token: DomainAuthorize, duration: Long = 3_600) {
        var userTokenCache: UserTokenCache? = mJsonCacheUtil.getValue(UserTokenCache.USER_TOKEN, UserTokenCache::class.java)
        if (userTokenCache == null) userTokenCache = UserTokenCache()
        userTokenCache.token = token
        mJsonCacheUtil.saveCache(UserTokenCache.USER_TOKEN, userTokenCache, duration)
    }


    /**
     * 保存上次登录后获取的用户信息
     * 以便下次打开app时即时加载
     *
     * @param info 本次登录获取的用户信息
     */
    fun saveUserInfo(info: DomainUserInfo.DataBean) {
        var userInfoCache: UserInfoCache? = mJsonCacheUtil.getValue(UserInfoCache.USER_INFO, UserInfoCache::class.java)
        if (userInfoCache == null) userInfoCache = UserInfoCache()
        userInfoCache.info = info
        mJsonCacheUtil.saveCache(UserInfoCache.USER_INFO, userInfoCache)
    }

    /**
     * 删除对应键的值
     *
     * @param key 键
     */
    fun deleteCache(key: String) {
        mJsonCacheUtil.deleteCache(key)
    }
}
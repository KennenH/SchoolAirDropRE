package com.example.schoolairdroprefactoredition.utils

import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo

class UserLoginCacheUtils {

    companion object {
        private var INSTANCE: UserLoginCacheUtils? = null
        fun getInstance() = INSTANCE
                ?: UserLoginCacheUtils().also {
                    INSTANCE = it
                }
    }

    private val mJsonCacheUtil: JsonCacheUtil = JsonCacheUtil.getInstance()

    /**
     * 保存用户上次登录获取的alipay id
     */
    fun saveUserAlipayID(alipayID: String) {
        mJsonCacheUtil.saveCache(JsonCacheConstantUtil.USER_ALIPAY_ID, alipayID)
    }

    /**
     * 获取[saveUserAlipayID]保存的信息
     */
    fun getUserAlipayID(): String? {
        return mJsonCacheUtil.getCache(JsonCacheConstantUtil.USER_ALIPAY_ID, String::class.java)
    }

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token    本次登录获取到的token
     * @param duration token持续时间
     */
    fun saveUserToken(token: DomainToken, duration: Long = 3_600) {
        val userTokenCache = mJsonCacheUtil.getCache(UserTokenCache.KEY, UserTokenCache::class.java)
                ?: UserTokenCache()
        userTokenCache.token = token
        mJsonCacheUtil.saveCache(UserTokenCache.KEY, userTokenCache, duration)
    }

    /**
     * 获取[saveUserToken]保存的信息
     */
    fun getUserToken(): DomainUserInfo.DataBean? {
        return mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)?.getLastLoggedAccount()
    }

    /**
     * 保存上次登录后获取的用户信息
     * 以便下次打开app时即时加载
     *
     * @param info 本次登录获取的用户信息
     */
    fun saveUserInfo(info: DomainUserInfo.DataBean) {
        mJsonCacheUtil.saveCache(
                UserInfoCache.KEY,
                mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)?.updateUserAccount(info))
    }

    /**
     * 获取[saveUserInfo]保存的信息
     */
    fun getUserInfo(): DomainToken? {
        return mJsonCacheUtil.getCache(UserTokenCache.KEY, UserTokenCache::class.java)?.token
    }

    /**
     * 退出当前账号
     * 不会删除当前账号在设备上的缓存
     */
    private fun quitCurrentUser() {
        mJsonCacheUtil.saveCache(
                UserInfoCache.KEY,
                mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)?.quitCurrentAccount())
    }

    /**
     * 删除对应键的值
     *
     * todo 删除缓存对于用户信息来说分两种，第一种仅退出账号不删除信息，第二种删除信息，此处是仅退出不删除
     *
     * @param key 键
     */
    fun deleteCache(key: String) {
        if (key == UserInfoCache.KEY) {
            quitCurrentUser()
        } else {
            mJsonCacheUtil.deleteCache(key)
        }
    }
}
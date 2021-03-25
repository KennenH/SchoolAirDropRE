package com.example.schoolairdroprefactoredition.cache.util

import android.util.Base64
import com.example.schoolairdroprefactoredition.cache.UserInfoCache
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import java.nio.charset.StandardCharsets

class UserLoginCacheUtil private constructor() {

    companion object {
        private var INSTANCE: UserLoginCacheUtil? = null
        fun getInstance() = INSTANCE
                ?: UserLoginCacheUtil().also {
                    INSTANCE = it
                }
    }

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token    本次登录获取到的token
     */
    fun saveUserToken(token: DomainToken) {
//        token.data.apply {
//            access_token = Base64.encodeToString(access_token.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)
//        }
        // 比token实际失效时间短半小时，防止用户上线前token有效而使用很短的时间token便过期
        mJsonCacheUtil.saveCache(UserTokenCache.KEY, UserTokenCache(token), token.data.expires_in * 750L)
    }

    /**
     * 获取[saveUserInfo]保存的信息，过期返回null
     */
    fun getUserToken(): DomainToken? {
        //        token?.data?.apply {
//            access_token = Base64.decode(access_token, Base64.DEFAULT).decodeToString()
//        }
        return mJsonCacheUtil.getCache(UserTokenCache.KEY, UserTokenCache::class.java)?.token
    }

    /*
     * 获取[saveUserInfo]保存的信息
     */
    fun getUserTokenAnyway(): DomainToken? {
        //        token?.data?.apply {
//            access_token = Base64.decode(access_token, Base64.DEFAULT).decodeToString()
//        }
        return mJsonCacheUtil.getCacheAnyway(UserTokenCache.KEY, UserTokenCache::class.java)?.token
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
     * 获取[saveUserToken]保存的信息
     */
    fun getUserInfo(): DomainUserInfo.DataBean? {
        return mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)?.getLastLoggedAccount()
    }

    /**
     * 退出当前账号
     */
    private fun quitCurrentUser() {
        mJsonCacheUtil.saveCache(
                UserInfoCache.KEY,
                mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache::class.java)?.quitCurrentAccount())
    }

    /**
     * 删除对应键的值
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
package com.example.schoolairdroprefactoredition.utils;

import com.example.schoolairdroprefactoredition.cache.UserInfoCache;
import com.example.schoolairdroprefactoredition.cache.UserTokenCache;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public class UserLoginCacheUtils {
    private static JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token 本次登录获取到的token
     */
    public static void saveUserToken(DomainAuthorize token) {
        UserTokenCache userTokenCache = mJsonCacheUtil.getValue(UserTokenCache.USER_TOKEN, UserTokenCache.class);

        if (userTokenCache == null) userTokenCache = new UserTokenCache();

        userTokenCache.setToken(token);
        mJsonCacheUtil.saveCache(UserTokenCache.USER_TOKEN, userTokenCache);
    }

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token    本次登录获取到的token
     * @param duration token持续时间
     */
    public static void saveUserToken(DomainAuthorize token, long duration) {
        UserTokenCache userTokenCache = mJsonCacheUtil.getValue(UserTokenCache.USER_TOKEN, UserTokenCache.class);

        if (userTokenCache == null) userTokenCache = new UserTokenCache();

        userTokenCache.setToken(token);
        mJsonCacheUtil.saveCache(UserTokenCache.USER_TOKEN, userTokenCache, duration);
    }

    /**
     * 保存上次登录后获取的用户信息
     * 以便下次打开app时即时加载
     *
     * @param info 本次登录获取的用户信息
     */
    public static void saveUserInfo(DomainUserInfo.DataBean info) {
        UserInfoCache userInfoCache = mJsonCacheUtil.getValue(UserInfoCache.USER_INFO, UserInfoCache.class);

        if (userInfoCache == null) userInfoCache = new UserInfoCache();

        userInfoCache.setInfo(info);
        mJsonCacheUtil.saveCache(UserInfoCache.USER_INFO, userInfoCache);
    }

    /**
     * 删除对应键的值
     *
     * @param key 键
     */
    public static void deleteCache(String key) {
        mJsonCacheUtil.deleteCache(key);
    }
}

package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.cache.UserInfoCache;
import com.example.schoolairdroprefactoredition.cache.UserTokenCache;
import com.example.schoolairdroprefactoredition.presenter.ICachePresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ICacheCallback;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;

public class CacheImpl implements ICachePresenter {

    private ICacheCallback mCallback;
    private JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

    @Override
    public void getUserInfoCache() {
        UserInfoCache userInfoCache = mJsonCacheUtil.getValue(UserInfoCache.USER_INFO, UserInfoCache.class);
        if (userInfoCache == null) userInfoCache = new UserInfoCache();
        if (mCallback != null)
            mCallback.onUserInfoCacheLoaded(userInfoCache.getInfo());
    }

    @Override
    public void getTokenCache() {
        UserTokenCache userTokenCache = mJsonCacheUtil.getValue(UserTokenCache.USER_TOKEN, UserTokenCache.class);
        if (userTokenCache == null) userTokenCache = new UserTokenCache();
        if (mCallback != null)
            mCallback.onTokenCacheLoaded(userTokenCache.getToken());
    }

    @Override
    public void registerCallback(ICacheCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ICacheCallback callback) {
        mCallback = null;
    }
}

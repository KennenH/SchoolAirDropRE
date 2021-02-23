package com.example.schoolairdroprefactoredition.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.cache.UserInfoCache;
import com.example.schoolairdroprefactoredition.cache.UserTokenCache;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;

import java.util.ArrayList;

public class AccountViewModel extends ViewModel {

    private final JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.getInstance();

    /**
     * 获取上次登录的用户的基本信息
     */
    public LiveData<DomainUserInfo.DataBean> getLastLoggedUserInfoCache() {
        MutableLiveData<DomainUserInfo.DataBean> mUserInfoCache = new MutableLiveData<>();
        UserInfoCache userInfoCache = mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache.class);
        if (userInfoCache == null) {
            userInfoCache = new UserInfoCache();
        }
        mUserInfoCache.setValue(userInfoCache.getLastLoggedAccount());
        return mUserInfoCache;
    }

    /**
     * 获取上次登录用户的token信息
     */
    public LiveData<DomainToken> getLastLoggedTokenCaChe() {
        MutableLiveData<DomainToken> mTokenCache = new MutableLiveData<>();
        UserTokenCache userTokenCache = mJsonCacheUtil.getCache(UserTokenCache.KEY, UserTokenCache.class);
        if (userTokenCache == null) {
            userTokenCache = new UserTokenCache();
        }
        mTokenCache.setValue(userTokenCache.getToken());
        return mTokenCache;
    }

    /**
     * 获取本设备上保存的所有用户信息
     */
    public LiveData<ArrayList<DomainUserInfo.DataBean>> getAllUserCacheOnThisDevice() {
        MutableLiveData<ArrayList<DomainUserInfo.DataBean>> mAllUsers = new MutableLiveData<>();
        UserInfoCache userInfoCache = mJsonCacheUtil.getCache(UserInfoCache.KEY, UserInfoCache.class);
        if (userInfoCache == null) {
            userInfoCache = new UserInfoCache();
        }
        mAllUsers.setValue(userInfoCache.getAllUsersOnThisDevice());
        return mAllUsers;
    }
}

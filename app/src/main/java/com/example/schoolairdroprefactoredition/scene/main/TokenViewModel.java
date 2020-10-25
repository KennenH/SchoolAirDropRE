package com.example.schoolairdroprefactoredition.scene.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.ICacheCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.CacheImpl;

public class TokenViewModel extends ViewModel implements ICacheCallback {

    private CacheImpl cacheImpl;

    private MutableLiveData<DomainUserInfo.DataBean> mUserInfoCache = new MutableLiveData<>();
    private MutableLiveData<DomainAuthorize> mTokenCache = new MutableLiveData<>();

    public TokenViewModel() {
        cacheImpl = new CacheImpl();
        cacheImpl.registerCallback(this);
    }

    public LiveData<DomainUserInfo.DataBean> getUserInfoCache() {
        cacheImpl.getUserInfoCache();
        return mUserInfoCache;
    }

    public LiveData<DomainAuthorize> getTokenCaChe() {
        cacheImpl.getTokenCache();
        return mTokenCache;
    }

    @Override
    public void onUserInfoCacheLoaded(DomainUserInfo.DataBean userInfo) {
        mUserInfoCache.postValue(userInfo);
    }

    @Override
    public void onTokenCacheLoaded(DomainAuthorize token) {
        mTokenCache.postValue(token);
    }

    @Override
    protected void onCleared() {
        cacheImpl.unregisterCallback(this);
    }
}

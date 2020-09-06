package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SettingsImpl;

public class SettingsViewModel extends BaseChildFragmentViewModel implements ISettingsCallback {

    private MutableLiveData<DomainAuthorizeGet> mAuthorizationKey = new MutableLiveData<>();
    private MutableLiveData<DomainAuthorize> mAuthorizedSession = new MutableLiveData<>();
    private MutableLiveData<DomainGetUserInfo> mUserInfo = new MutableLiveData<>();

    private SettingsImpl settingsImpl;

    public SettingsViewModel() {
        settingsImpl = new SettingsImpl();
        settingsImpl.registerCallback(this);
    }

    public LiveData<DomainAuthorizeGet> getPublicKey() {
        settingsImpl.getPublicKey();
        return mAuthorizationKey;
    }

    public LiveData<DomainAuthorize> authorizeWithAlipayID(String cookie, String grantType, String clientID, String clientSecret, String rawAlipay, String publicKey) {
        settingsImpl.postAlipayIDRSA(cookie, grantType, clientID, clientSecret, rawAlipay, publicKey);
        return mAuthorizedSession;
    }

    public LiveData<DomainGetUserInfo> getUserInfo(String token) {
        settingsImpl.getUserInfo(token);
        return mUserInfo;
    }

    @Override
    public void onPublicKeyGot(DomainAuthorizeGet authorization) {
        mAuthorizationKey.postValue(authorization);
    }

    @Override
    public void onAuthorizationSuccess(DomainAuthorize authorization) {
        mAuthorizedSession.postValue(authorization);
    }

    @Override
    public void onUserInfoLoaded(DomainGetUserInfo info) {
        mUserInfo.postValue(info);
    }

    @Override
    protected void onCleared() {
        settingsImpl.unregisterCallback(this);
    }


    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }
}

package com.example.schoolairdroprefactoredition.scene.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.ILoginCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.LoginImpl;

public class LoginViewModel extends BaseStateViewModel implements ILoginCallback {

    private MutableLiveData<DomainAuthorizeGet> mAuthorizationKey = new MutableLiveData<>();
    private MutableLiveData<DomainAuthorize> mAuthorizedSession = new MutableLiveData<>();
    private MutableLiveData<DomainUserInfo> mUserInfo = new MutableLiveData<>();

    private LoginImpl loginImpl;

    public LoginViewModel() {
        loginImpl = new LoginImpl();
        loginImpl.registerCallback(this);
    }

    public void logout() {
        loginImpl.logout();
    }

    public LiveData<DomainAuthorizeGet> getPublicKey() {
        loginImpl.getPublicKey();
        return mAuthorizationKey;
    }

    public LiveData<DomainAuthorize> authorizeWithAlipayID(String cookie, String grantType, String clientID, String clientSecret, String rawAlipay, String publicKey) {
        loginImpl.postAlipayIDRSA(cookie, grantType, clientID, clientSecret, rawAlipay, publicKey);
        return mAuthorizedSession;
    }

    public LiveData<DomainUserInfo> getUserInfo(String token) {
        loginImpl.getUserInfo(token);
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
    public void onUserInfoLoaded(DomainUserInfo info) {
        mUserInfo.postValue(info);
    }

    @Override
    public void onLogout() {

    }

    @Override
    protected void onCleared() {
        loginImpl.unregisterCallback(this);
    }


    @Override
    public void onError() {

    }

}

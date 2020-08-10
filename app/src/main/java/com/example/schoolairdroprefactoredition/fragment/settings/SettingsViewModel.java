package com.example.schoolairdroprefactoredition.fragment.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizePost;
import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SettingsImpl;

public class SettingsViewModel extends ViewModel implements ISettingsCallback {

    private MutableLiveData<DomainAuthorizeGet> mAuthorizationKey = new MutableLiveData<>();
    private MutableLiveData<String> mAuthorizedSession = new MutableLiveData<>();

    private SettingsImpl settingsImpl;

    public SettingsViewModel() {
        settingsImpl = new SettingsImpl();
        settingsImpl.registerCallback(this);
    }

    public LiveData<DomainAuthorizeGet> getPublicKey() {
        settingsImpl.getPublicKey();
        return mAuthorizationKey;
    }

    public LiveData<String> authorizeWithAlipayID(String publicKey, String alipayID){
        settingsImpl.postAlipayIDRSA(publicKey, alipayID);
        return mAuthorizedSession;
    }

    @Override
    public void onPublicKeyGot(DomainAuthorizeGet authorization) {
        mAuthorizationKey.postValue(authorization);
    }

    @Override
    public void onAuthorizationSuccess(String authorization) {
        mAuthorizedSession.postValue(authorization);
    }

    @Override
    public void onAuthorizationFailed() {

    }

    @Override
    protected void onCleared() {
        settingsImpl.unregisterCallback(this);
    }
}

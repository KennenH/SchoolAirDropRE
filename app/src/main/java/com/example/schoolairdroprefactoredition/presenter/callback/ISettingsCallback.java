package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;

public interface ISettingsCallback extends IBaseCallback {
    void onPublicKeyGot(DomainAuthorizeGet authorization);

    void onAuthorizationSuccess(DomainAuthorize authorization);

    void onUserInfoLoaded(DomainGetUserInfo info);
}

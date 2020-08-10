package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizePost;

public interface ISettingsCallback {
    void onPublicKeyGot(DomainAuthorizeGet authorization);

    void onAuthorizationSuccess(String authorization);

    void onAuthorizationFailed();
}

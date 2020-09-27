package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public interface ILoginCallback {
    /**
     * 成功获取公钥
     */
    void onPublicKeyGot(DomainAuthorizeGet authorization);

    /**
     * 成功获取token
     */
    void onAuthorizationSuccess(DomainAuthorize authorization);

    /**
     * 用户信息加载
     */
    void onUserInfoLoaded(DomainUserInfo info);

    void onLogout();

    /**
     * 除已排除外的登录失败
     */
    void onLoginError();

    /**
     * token过期
     */
    void onTokenInvalid();
}

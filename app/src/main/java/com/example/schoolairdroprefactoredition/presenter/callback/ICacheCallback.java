package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public interface ICacheCallback {
    void onUserInfoCacheLoaded(DomainUserInfo.DataBean userInfo);

    void onTokenCacheLoaded(DomainAuthorize token);
}

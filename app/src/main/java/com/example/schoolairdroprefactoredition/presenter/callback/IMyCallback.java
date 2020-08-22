package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;

public interface IMyCallback extends IBaseCallback {
    void onUserInfoLoaded(DomainGetUserInfo data);
}

package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public interface IMyCallback extends IBaseCallback {
    void onUserInfoLoaded(DomainUserInfo data);
}

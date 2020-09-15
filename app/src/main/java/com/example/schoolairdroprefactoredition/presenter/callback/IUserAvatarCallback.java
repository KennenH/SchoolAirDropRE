package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;

public interface IUserAvatarCallback extends IBaseCallback {
    void onUpdateSuccess(DomainAvatarUpdateResult result);
}

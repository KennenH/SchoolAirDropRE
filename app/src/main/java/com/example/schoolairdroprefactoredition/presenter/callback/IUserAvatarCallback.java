package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdate;

public interface IUserAvatarCallback extends IBaseCallback {
    void onSent(DomainAvatarUpdate result);
}

package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;

public interface IUserAvatarPresenter extends IBasePresenter<IUserAvatarCallback> {
    void updateAvatar(String token, String img);
}

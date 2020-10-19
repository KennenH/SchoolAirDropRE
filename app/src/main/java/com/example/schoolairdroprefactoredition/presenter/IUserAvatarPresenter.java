package com.example.schoolairdroprefactoredition.presenter;

import android.content.Context;

import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;

public interface IUserAvatarPresenter extends IBasePresenter<IUserAvatarCallback> {
    void updateAvatar(Context context, String token, String img);
}

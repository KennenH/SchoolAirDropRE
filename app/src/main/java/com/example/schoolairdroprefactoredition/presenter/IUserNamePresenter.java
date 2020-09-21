package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IResultCallback;

public interface IUserNamePresenter extends IBasePresenter<IResultCallback> {
    void rename(String token, String name);
}

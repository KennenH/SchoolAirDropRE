package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IUserNameCallback;

public interface IUserNamePresenter extends IBasePresenter<IUserNameCallback> {
    void rename(String token, String name);
}

package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;

public interface IMyPresenter extends IBasePresenter<IMyCallback> {
    void getUserInfo();
}

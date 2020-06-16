package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestUserInfoBean;
import com.example.schoolairdroprefactoredition.presenter.IMyPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;

public class MyImpl implements IMyPresenter {

    private IMyCallback mCallback = null;

    @Override
    public void getUserInfo() {
        // todo 请求网络数据

        mCallback.onUserInfoLoaded(new TestUserInfoBean());
    }

    @Override
    public void registerCallback(IMyCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IMyCallback callback) {
        mCallback = null;
    }
}

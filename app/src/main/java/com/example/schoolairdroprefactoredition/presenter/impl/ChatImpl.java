package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.presenter.IChatPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IChatCallback;

public class ChatImpl implements IChatPresenter {

    private IChatCallback mCallback;

    @Override
    public void getChatHistories() {

    }

    @Override
    public void getMessage() {

    }

    @Override
    public void registerCallback(IChatCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IChatCallback callback) {
        mCallback = null;
    }
}

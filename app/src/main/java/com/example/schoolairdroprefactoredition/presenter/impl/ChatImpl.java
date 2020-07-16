package com.example.schoolairdroprefactoredition.presenter.impl;

import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.presenter.IChatPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IChatCallback;

public class ChatImpl extends ViewModel implements IChatPresenter {

    private IChatCallback mCallback = null;

    @Override
    public void getChatHistories() {
//        mCallback.onHistoriesLoaded(data);
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

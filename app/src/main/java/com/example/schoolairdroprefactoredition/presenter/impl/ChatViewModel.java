package com.example.schoolairdroprefactoredition.presenter.impl;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainChatBean;
import com.example.schoolairdroprefactoredition.presenter.IChatPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IChatCallback;

public class ChatViewModel extends ViewModel implements IChatCallback {

    private ChatImpl chatImpl;

    private MutableLiveData<DomainChatBean> mChatList = new MutableLiveData<>();


    public ChatViewModel() {
        chatImpl = new ChatImpl();
        chatImpl.registerCallback(this);
    }

    @Override
    public void onHistoriesLoaded(DomainChatBean data) {

    }

    @Override
    public void onHistoriesEmpty() {

    }

    @Override
    public void onHistoriesLoadFailed() {

    }
}

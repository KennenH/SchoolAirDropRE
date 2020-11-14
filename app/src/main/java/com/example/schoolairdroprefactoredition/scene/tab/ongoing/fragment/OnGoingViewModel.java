package com.example.schoolairdroprefactoredition.scene.tab.ongoing.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;
import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.OnGoingImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class OnGoingViewModel extends BaseStateViewModel implements IOnGoingCallback {

    private OnGoingImpl onGoingImpl;

    private MutableLiveData<DomainOnGoing> mReceived = new MutableLiveData<>();
    private MutableLiveData<DomainOnGoing> mSent = new MutableLiveData<>();

    public LiveData<DomainOnGoing> getSent(String token) {
        onGoingImpl.getMySent(token);
        return mSent;
    }

    public MutableLiveData<DomainOnGoing> getReceived(String token) {
        onGoingImpl.getMyReceived(token);
        return mReceived;
    }

    public OnGoingViewModel() {
        onGoingImpl = new OnGoingImpl();
        onGoingImpl.registerCallback(this);
    }


    @Override
    public void onEventMySentLoaded(DomainOnGoing data) {
        mSent.postValue(data);
    }

    @Override
    public void onEventMyReceivedLoaded(DomainOnGoing data) {
        mReceived.postValue(data);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onGoingImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onError();
        }
    }
}

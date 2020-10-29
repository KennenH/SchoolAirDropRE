package com.example.schoolairdroprefactoredition.scene.ongoing.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;
import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.OnGoingImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class OnGoingViewModel extends BaseStateViewModel implements IOnGoingCallback {

    private OnGoingImpl onGoingImpl;

    private MutableLiveData<List<DomainOnGoing.Data>> mReceived = new MutableLiveData<>();
    private MutableLiveData<List<DomainOnGoing.Data>> mSent = new MutableLiveData<>();

    public LiveData<List<DomainOnGoing.Data>> getSent(String token) {
        onGoingImpl.getMySent(token);
        return mSent;
    }

    public MutableLiveData<List<DomainOnGoing.Data>> getReceived(String token) {
        onGoingImpl.getMyReceived(token);
        return mReceived;
    }

    public OnGoingViewModel() {
        onGoingImpl = new OnGoingImpl();
        onGoingImpl.registerCallback(this);
    }


    @Override
    public void onEventMySentLoaded(List<DomainOnGoing.Data> data) {
        mSent.postValue(data);
    }

    @Override
    public void onEventMyReceivedLoaded(List<DomainOnGoing.Data> data) {
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

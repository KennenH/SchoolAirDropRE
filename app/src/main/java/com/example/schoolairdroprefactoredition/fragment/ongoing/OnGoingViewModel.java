package com.example.schoolairdroprefactoredition.fragment.ongoing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.OnGoingImpl;

import java.util.List;

public class OnGoingViewModel extends ViewModel implements IOnGoingCallback {

    private OnGoingImpl onGoingImpl;

    private MutableLiveData<List<TestOnGoingBean>> mUnPaid = new MutableLiveData<>();
    private MutableLiveData<List<TestOnGoingBean>> mUnDelivered = new MutableLiveData<>();

    public LiveData<List<TestOnGoingBean>> getUnPaid() {
        onGoingImpl.getUnPaid();
        return mUnPaid;
    }

    public MutableLiveData<List<TestOnGoingBean>> getUnDelivered() {
        onGoingImpl.getUnDelivered();
        return mUnDelivered;
    }

    public OnGoingViewModel() {
        onGoingImpl = new OnGoingImpl();
        onGoingImpl.registerCallback(this);
    }


    @Override
    public void onUnPaidLoaded(List<TestOnGoingBean> data) {
        mUnPaid.postValue(data);
    }

    @Override
    public void onUnDeliveredLoaded(List<TestOnGoingBean> data) {
        mUnDelivered.postValue(data);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onGoingImpl.unregisterCallback(this);
    }
}

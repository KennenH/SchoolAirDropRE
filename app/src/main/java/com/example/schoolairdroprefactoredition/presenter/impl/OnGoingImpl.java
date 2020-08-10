package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;
import com.example.schoolairdroprefactoredition.presenter.IOnGoingPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;

import java.util.Arrays;

public class OnGoingImpl implements IOnGoingPresenter {

    private IOnGoingCallback mCallback;

    @Override
    public void getUnPaid() {
        TestOnGoingBean[] beans = new TestOnGoingBean[5];
        for (int i = 0; i < 5; i++) beans[i] = new TestOnGoingBean();
        mCallback.onUnPaidLoaded(Arrays.asList(beans));
    }

    @Override
    public void getUnDelivered() {
        TestOnGoingBean[] beans = new TestOnGoingBean[5];
        for (int i = 0; i < 5; i++) beans[i] = new TestOnGoingBean();
        mCallback.onUnDeliveredLoaded(Arrays.asList(beans));
    }

    @Override
    public void registerCallback(IOnGoingCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IOnGoingCallback callback) {
        mCallback = null;
    }

}

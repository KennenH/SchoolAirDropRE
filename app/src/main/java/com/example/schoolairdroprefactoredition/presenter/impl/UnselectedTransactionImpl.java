package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;
import com.example.schoolairdroprefactoredition.presenter.IUnselectedTransactionPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IUnselectedTransactionCallback;

import java.util.Arrays;

public class UnselectedTransactionImpl implements IUnselectedTransactionPresenter {

    private IUnselectedTransactionCallback mCallback;

    @Override
    public void getUnselectedTransaction() {
        DomainUnselectedTransaction[] list = new DomainUnselectedTransaction[3];
        for (int i = 0; i < 3; i++) list[i] = new DomainUnselectedTransaction();

        list[0].setTitle("哈哈哈哈哈啊哈哈哈");
        list[1].setTitle("寄卡升级的了解啦首付款");
        list[2].setTitle("吉拉斯的卡是多少啊");
        if (mCallback != null)
            mCallback.onTransactionLoaded(Arrays.asList(list));
    }

    @Override
    public void registerCallback(IUnselectedTransactionCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IUnselectedTransactionCallback callback) {
        mCallback = null;
    }
}

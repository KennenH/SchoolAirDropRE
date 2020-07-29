package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.ISoldPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;

import java.util.Arrays;

public class SoldImpl implements ISoldPresenter {
    private ISoldCallback mCallback;

    @Override
    public void getSoldList() {
        TestSSBItemBean[] beans = new TestSSBItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestSSBItemBean();

        beans[0].setTitle("一杯mojito");
        beans[1].setTitle("IBasePresenter");
        beans[2].setTitle("ISellingCallback");
        beans[3].setTitle("哈哈哈哈哈哈哈哈");
        beans[4].setTitle("fatty boom boom");
        beans[5].setTitle("mud fucker");
        beans[6].setTitle("ISellingPresenter");
        beans[7].setTitle("shit shit shit");
        beans[8].setTitle("豆腐一块两块，两块一块啦");
        beans[9].setTitle("SellingImpl");

        mCallback.onSoldListLoaded(Arrays.asList(beans));
    }

    @Override
    public void registerCallback(ISoldCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISoldCallback callback) {
        mCallback = null;
    }
}

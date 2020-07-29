package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.IBoughtPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;

import java.util.Arrays;

public class BoughtImpl implements IBoughtPresenter {

    private IBoughtCallback mCallback;

    @Override
    public void getBoughtList() {
        // 网络请求在售数据

        TestSSBItemBean[] beans = new TestSSBItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestSSBItemBean();

        beans[0].setTitle("shit shit shit");
        beans[1].setTitle("SellingImpl");
        beans[2].setTitle("IBasePresenter");
        beans[3].setTitle("fatty boom boom");
        beans[4].setTitle("豆腐一块两块，两块一块啦");
        beans[5].setTitle("哈哈哈哈哈哈哈哈");
        beans[6].setTitle("ISellingCallback");
        beans[7].setTitle("ISellingPresenter");
        beans[8].setTitle("mud fucker");
        beans[9].setTitle("一杯mojito");

        mCallback.onBoughtListLoaded(Arrays.asList(beans));
    }

    @Override
    public void registerCallback(IBoughtCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IBoughtCallback callback) {
        mCallback = null;
    }
}

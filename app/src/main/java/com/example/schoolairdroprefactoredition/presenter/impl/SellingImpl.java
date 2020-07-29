package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.ISellingPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;

import java.util.Arrays;

public class SellingImpl implements ISellingPresenter {

    private ISellingCallback mCallback;

    @Override
    public void getSellingList() {
        // 网络请求在售数据

        TestSSBItemBean[] beans = new TestSSBItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestSSBItemBean();

        beans[0].setTitle("fatty boom boom");
        beans[1].setTitle("豆腐一块两块，两块一块啦");
        beans[2].setTitle("哈哈哈哈哈哈哈哈");
        beans[3].setTitle("mud fucker");
        beans[4].setTitle("shit shit shit");
        beans[5].setTitle("一杯mojito");
        beans[6].setTitle("IBasePresenter");
        beans[7].setTitle("ISellingPresenter");
        beans[8].setTitle("ISellingCallback");
        beans[9].setTitle("SellingImpl");

        mCallback.onSellingListLoaded(Arrays.asList(beans));
    }

    @Override
    public void registerCallback(ISellingCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISellingCallback callback) {
        mCallback = null;
    }
}

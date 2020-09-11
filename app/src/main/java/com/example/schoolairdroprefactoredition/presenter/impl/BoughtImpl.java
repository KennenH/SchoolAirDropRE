package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
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

        DomainGoodsInfo.DataBean[] beans = new DomainGoodsInfo.DataBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new DomainGoodsInfo.DataBean();

        beans[0].setGoods_name("shit shit shit");
        beans[1].setGoods_name("SellingImpl");
        beans[2].setGoods_name("IBasePresenter");
        beans[3].setGoods_name("fatty boom boom");
        beans[4].setGoods_name("豆腐一块两块，两块一块啦");
        beans[5].setGoods_name("哈哈哈哈哈哈哈哈");
        beans[6].setGoods_name("ISellingCallback");
        beans[7].setGoods_name("ISellingPresenter");
        beans[8].setGoods_name("mud fucker");
        beans[9].setGoods_name("一杯mojito");

        DomainGoodsInfo info = new DomainGoodsInfo();
        info.setData(Arrays.asList(beans));

        mCallback.onBoughtListLoaded(info);
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

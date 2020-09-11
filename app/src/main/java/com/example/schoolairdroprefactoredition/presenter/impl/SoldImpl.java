package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.ISoldPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;

import java.util.Arrays;

public class SoldImpl implements ISoldPresenter {
    private ISoldCallback mCallback;

    @Override
    public void getSoldList() {
        DomainGoodsInfo.DataBean[] beans = new DomainGoodsInfo.DataBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new DomainGoodsInfo.DataBean();

        beans[0].setGoods_name("一杯mojito");
        beans[1].setGoods_name("IBasePresenter");
        beans[2].setGoods_name("ISellingCallback");
        beans[3].setGoods_name("哈哈哈哈哈哈哈哈");
        beans[4].setGoods_name("fatty boom boom");
        beans[5].setGoods_name("mud fucker");
        beans[6].setGoods_name("ISellingPresenter");
        beans[7].setGoods_name("shit shit shit");
        beans[8].setGoods_name("豆腐一块两块，两块一块啦");
        beans[9].setGoods_name("SellingImpl");

        DomainGoodsInfo info = new DomainGoodsInfo();
        info.setData(Arrays.asList(beans));

        mCallback.onSoldListLoaded(info);
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

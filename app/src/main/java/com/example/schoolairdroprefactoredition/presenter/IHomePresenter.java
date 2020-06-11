package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取附近在售商品信息
     */
    void getNearbyGoodsInfo();

    /**
     * 获取最新消息的信息
     */
    void getNews();
}

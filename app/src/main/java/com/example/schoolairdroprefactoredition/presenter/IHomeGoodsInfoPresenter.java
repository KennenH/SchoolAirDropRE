package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;

public interface IHomeGoodsInfoPresenter extends IBasePresenter<IHomeGoodsInfoCallback> {
    /**
     * 获取附近在售商品信息
     */
    void getNearbyGoods(int num, double longitude, double latitude);
}

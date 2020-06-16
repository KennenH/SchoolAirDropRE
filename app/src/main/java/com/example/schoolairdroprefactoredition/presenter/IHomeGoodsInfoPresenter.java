package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;

public interface IHomeGoodsInfoPresenter extends IBasePresenter<IHomeGoodsInfoCallback> {
    /**
     * 获取附近在售商品信息
     *
     * @param num 请求item条数
     */
    void getNearbyGoodsInfo(int num);
}

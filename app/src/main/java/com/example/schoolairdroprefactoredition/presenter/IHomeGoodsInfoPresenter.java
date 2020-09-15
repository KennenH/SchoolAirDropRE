package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;

public interface IHomeGoodsInfoPresenter extends IBasePresenter<IHomeGoodsInfoCallback> {
    void getNearbyGoods( int page, double longitude, double latitude);
}

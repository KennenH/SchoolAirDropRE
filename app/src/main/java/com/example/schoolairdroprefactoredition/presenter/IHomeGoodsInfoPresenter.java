package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;

public interface IHomeGoodsInfoPresenter extends IBasePresenter<IHomeGoodsInfoCallback> {
    void getNearbyGoods(String token, int page, double longitude, double latitude);
}

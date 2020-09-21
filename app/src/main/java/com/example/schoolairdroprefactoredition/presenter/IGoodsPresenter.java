package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;

public interface IGoodsPresenter extends IBasePresenter<IGoodsCallback> {
    /**
     * 发起报价
     *
     * @param goodsID    物品id
     * @param quotePrice 报价价格
     */
    void quoteRequest(String token, String goodsID, String quotePrice);

    /**
     * 收藏物品
     *
     * @param goodsID 物品id
     */
    void favorite(String token, int goodsID);
}


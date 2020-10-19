package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;

public interface IGoodsPresenter extends IBasePresenter<IGoodsCallback> {
    /**
     * 发起报价
     *
     * @param goodsID    物品id
     * @param quotePrice 报价价格
     */
    void quoteItem(String token, String goodsID, String quotePrice);

    /**
     * 收藏物品
     *
     * @param goodsID 物品id
     */
    void favoriteItem(String token, String goodsID);

    /**
     * 查询物品是否被收藏
     */
    void isItemFavored(String token, String goodsID);

    /**
     * 取消收藏物品
     */
    void unFavorItem(String token, String goodsID);
}


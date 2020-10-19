package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;

public interface ISSBPresenter extends IBasePresenter<ISSBCallback> {
    /**
     * 获取自己的已售列表
     */
    void getSoldList(String token, int page);

    /**
     * 获取自己的已购列表
     */
    void getBoughtList(String token, int page);

    /**
     * 获取自己的在售列表
     */
    void getSellingList(String token, int page);

    /**
     * 下架物品
     */
    void unListItem(String token, String goodsID);

    /**
     * 查看他人的在售物品页面
     */
    void getSellingByUID(int userID);
}

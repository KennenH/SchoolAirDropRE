package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import java.util.List;

public interface IHomeGoodsInfoCallback {
    /**
     * 正在加载标志
     */
    void onGoodsInfoLoading();

    /**
     * 附近商品信息被加载
     */
    void onGoodsInfoLoaded(List<TestGoodsItemBean> goodsData);

    /**
     * 附近没有更多商品
     */
    void onGoodsInfoEmpty();
}

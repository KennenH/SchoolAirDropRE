package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;

import java.util.List;

public interface IHomeGoodsInfoCallback extends IBaseCallback {
    /**
     * 附近商品信息被加载
     */
    void onNearbyGoodsLoaded(List<HomeGoodsListInfo.DataBean> goodsData);
}

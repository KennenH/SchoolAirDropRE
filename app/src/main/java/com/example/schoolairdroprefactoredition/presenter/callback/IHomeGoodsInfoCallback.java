package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;

import java.util.List;

public interface IHomeGoodsInfoCallback extends IBaseCallback {
    /**
     * 附近商品信息被加载
     */
    void onNearbyGoodsLoaded(List<DomainGoodsInfo.DataBean> goodsData);
}

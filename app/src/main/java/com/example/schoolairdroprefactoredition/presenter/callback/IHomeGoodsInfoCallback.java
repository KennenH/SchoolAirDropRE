package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import java.util.List;
import java.util.Map;

public interface IHomeGoodsInfoCallback extends IBaseCallback {
    /**
     * 附近商品信息被加载
     */
    void onNearbyGoodsLoaded(List<DomainGoodsInfo.GoodsInfoBean> goodsData);
}

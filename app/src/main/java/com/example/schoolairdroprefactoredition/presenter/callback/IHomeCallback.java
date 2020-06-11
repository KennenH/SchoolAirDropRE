package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainNews;

public interface IHomeCallback {
    /**
     * 附近商品信息被加载
     * @param domainGoodsInfo 附近商品信息
     */
    void onGoodsInfoLoaded(DomainGoodsInfo domainGoodsInfo);

    /**
     * 最新消息数据加载
     * @param domainNews 新闻数据
     */
    void onNewsLoaded(DomainNews domainNews);
}

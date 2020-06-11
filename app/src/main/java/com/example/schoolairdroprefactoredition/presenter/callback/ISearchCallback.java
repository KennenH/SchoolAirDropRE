package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;

public interface ISearchCallback {

    /**
     * 搜索结果加载完毕
     * @param domainGoodsInfo
     */
    void onSearchResultLoaded(DomainGoodsInfo domainGoodsInfo);

    /**
     * 搜索历史加载完毕
     * @param domainSearchItem
     */
    void onSearchHistoryLoaded(DomainSearchItems domainSearchItem);

    /**
     * 搜索建议加载完毕
     * @param domainSearchItem
     */
    void onSearchSuggestionLoaded(DomainSearchItems domainSearchItem);
}

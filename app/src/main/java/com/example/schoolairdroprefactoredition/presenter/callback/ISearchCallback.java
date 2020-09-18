package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.cache.SearchHistories;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {
    /**
     * 搜索结果正在加载
     */
    void onSearchResultLoading();

    /**
     * 搜索结果加载完毕
     */
    void onSearchResultLoaded(List<DomainGoodsInfo.DataBean> goodsInfoBeans);

    /**
     * 搜索历史加载完毕
     */
    void onSearchHistoryLoaded(SearchHistories domainSearchItem);

    /**
     * 搜索建议加载完毕
     */
    void onSearchSuggestionLoaded(SearchSuggestionBean domainSearchItem);


    /**
     * 搜索结果没有更多
     */
    void onResultEmpty();
}

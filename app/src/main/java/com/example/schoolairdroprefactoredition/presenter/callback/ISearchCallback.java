package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import java.util.List;

public interface ISearchCallback {
    /**
     * 搜索结果正在加载
     */
    void onSearchResultLoading();

    /**
     * 搜索结果加载完毕
     */
    void onSearchResultLoaded(List<TestGoodsItemBean> testGoodsDetailData);

    /**
     * 搜索历史加载完毕
     */
    void onSearchHistoryLoaded(DomainSearchItems domainSearchItem);

    /**
     * 搜索建议加载完毕
     */
    void onSearchSuggestionLoaded(SearchSuggestionBean domainSearchItem);


    /**
     * 搜索结果没有更多
     */
    void onResultEmpty();
}

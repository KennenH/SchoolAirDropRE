package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.cache.SearchHistories;
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {
    /**
     * 搜索物品
     */
    void onSearchResultLoaded(List<HomeGoodsListInfo.DataBean> goodsInfoBeans);

    /**
     * 搜索历史
     */
    void onSearchHistoryLoaded(SearchHistories domainSearchItem);

    /**
     * 搜索建议
     */
    void onSearchSuggestionLoaded(SearchSuggestionBean domainSearchItem);
}

package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {
    /**
     * 获取搜索结果
     */
    void getSearchResult(String token, double longitude, double latitude, String key, boolean isLoadMore);

    /**
     * 获取搜索历史
     */
    void getSearchHistory();

    /**
     * 获取搜索建议
     */
    void getSearchSuggestion(String key);

    /**
     * 删除历史记录
     */
    void deleteHistories();
}

package com.example.schoolairdroprefactoredition.scene.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.cache.SearchHistories;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SearchImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class SearchViewModel extends BaseStateViewModel implements ISearchCallback {
    private String lastSearchedKey;

    private SearchImpl mSearchImpl;

    private MutableLiveData<List<DomainGoodsInfo.DataBean>> mSearchResults = new MutableLiveData<>();
    private MutableLiveData<SearchHistories> mSearchHistories = new MutableLiveData<>();
    private MutableLiveData<SearchSuggestionBean> mSearchSuggestions = new MutableLiveData<>();

    public SearchViewModel() {
        mSearchImpl = new SearchImpl();
        mSearchImpl.registerCallback(this);
    }

    @Override
    public void onSearchResultLoading() {
        //todo 正在加载
    }

    @Override
    public void onSearchResultLoaded(List<DomainGoodsInfo.DataBean> beans) {
        mSearchResults.postValue(beans);
    }

    @Override
    public void onSearchHistoryLoaded(SearchHistories histories) {
        mSearchHistories.postValue(histories);
    }

    @Override
    public void onSearchSuggestionLoaded(SearchSuggestionBean domainSearchItems) {
        mSearchSuggestions.postValue(domainSearchItems);
    }

    @Override
    public void onResultEmpty() {
        //todo 没有更多
    }

    public void deleteHistories() {
        mSearchImpl.deleteHistories();
    }

    public LiveData<List<DomainGoodsInfo.DataBean>> getLastSearchedResult(String token, double longitude, double latitude) {
        mSearchImpl.getSearchResult(token, longitude, latitude, lastSearchedKey, true);
        return mSearchResults;
    }

    public LiveData<List<DomainGoodsInfo.DataBean>> getSearchResult(String token, double longitude, double latitude, String key) {
        lastSearchedKey = key;
        mSearchImpl.getSearchResult(token, longitude, latitude, key, false);
        return mSearchResults;
    }

    public LiveData<SearchHistories> getSearchHistories() {
        mSearchImpl.getSearchHistory();
        return mSearchHistories;
    }

    public LiveData<SearchSuggestionBean> getSearchSuggestion(String input) {
        mSearchImpl.getSearchSuggestion(input);
        return mSearchSuggestions;
    }

    @Override
    protected void onCleared() {
        mSearchImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}

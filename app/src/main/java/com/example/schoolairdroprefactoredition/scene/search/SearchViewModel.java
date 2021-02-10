package com.example.schoolairdroprefactoredition.scene.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.cache.SearchHistories;
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;
import com.example.schoolairdroprefactoredition.domain.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SearchImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class SearchViewModel extends BaseStateViewModel implements ISearchCallback {
    private String lastSearchedKey;
    private double lastLongitude;
    private double lastLatitude;
    private int page = 0;

    private final SearchImpl mSearchImpl;

    private final MutableLiveData<List<HomeGoodsListInfo.DataBean>> mSearchResults = new MutableLiveData<>();
    private final MutableLiveData<SearchHistories> mSearchHistories = new MutableLiveData<>();
    private final MutableLiveData<SearchSuggestionBean> mSearchSuggestions = new MutableLiveData<>();

    public SearchViewModel() {
        mSearchImpl = SearchImpl.getInstance();
        mSearchImpl.registerCallback(this);
    }

    @Override
    public void onSearchResultLoaded(List<HomeGoodsListInfo.DataBean> beans) {
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

    public void deleteHistories() {
        mSearchImpl.deleteHistories();
    }

    public LiveData<List<HomeGoodsListInfo.DataBean>> getSearchResult() {
        mSearchImpl.getSearchResult(page++, lastLongitude, lastLatitude, lastSearchedKey, true);
        return mSearchResults;
    }

    public LiveData<List<HomeGoodsListInfo.DataBean>> getSearchResult(double longitude, double latitude, String key) {
        page = 0;
        lastSearchedKey = key;
        lastLongitude = longitude;
        lastLatitude = latitude;

        mSearchImpl.getSearchResult(page++, longitude, latitude, key, false);
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

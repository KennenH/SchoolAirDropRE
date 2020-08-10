package com.example.schoolairdroprefactoredition.fragment.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SearchImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import java.util.List;

public class SearchViewModel extends ViewModel implements ISearchCallback {
    private String lastSearchedKey;

    private SearchImpl mSearchImpl;

    private MutableLiveData<List<DomainGoodsInfo.GoodsInfoBean>> mSearchResults;
    private MutableLiveData<DomainSearchItems> mSearchHistories;
    private MutableLiveData<SearchSuggestionBean> mSearchSuggestions;

    public SearchViewModel() {
        mSearchImpl = new SearchImpl();
        mSearchImpl.registerCallback(this);
    }

    @Override
    public void onSearchResultLoading() {
        //todo 正在加载
    }

    @Override
    public void onSearchResultLoaded(List<DomainGoodsInfo.GoodsInfoBean> domainGoodsInfo) {
        mSearchResults = new MutableLiveData<>();
        mSearchResults.setValue(domainGoodsInfo);
    }

    @Override
    public void onSearchHistoryLoaded(DomainSearchItems domainSearchItems) {
        mSearchHistories = new MutableLiveData<>();
        mSearchHistories.setValue(domainSearchItems);
    }

    @Override
    public void onSearchSuggestionLoaded(SearchSuggestionBean domainSearchItems) {
        mSearchSuggestions = new MutableLiveData<>();
        mSearchSuggestions.setValue(domainSearchItems);
    }

    @Override
    public void onResultEmpty() {
        //todo 没有更多
    }

    public LiveData<List<DomainGoodsInfo.GoodsInfoBean>> getLastSearchedResult() {
        mSearchImpl.getSearchResult(lastSearchedKey);
        return mSearchResults;
    }

    public LiveData<List<DomainGoodsInfo.GoodsInfoBean>> getSearchResult(String key) {
        lastSearchedKey = key;
        mSearchImpl.getSearchResult(key);
        return mSearchResults;
    }

    public LiveData<DomainSearchItems> getSearchHistories() {
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
}

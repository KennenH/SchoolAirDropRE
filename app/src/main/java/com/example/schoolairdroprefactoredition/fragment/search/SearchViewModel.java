package com.example.schoolairdroprefactoredition.fragment.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SearchImpl;

public class SearchViewModel extends ViewModel implements ISearchCallback {
    private SearchImpl mSearchImpl;

    private MutableLiveData<DomainGoodsInfo> mSearchResults;
    private MutableLiveData<DomainSearchItems> mSearchHistories;
    private MutableLiveData<DomainSearchItems> mSearchSuggestions;

    public SearchViewModel() {
        mSearchImpl = new SearchImpl();
        mSearchImpl.registerCallback(this);
    }

    @Override
    public void onSearchResultLoaded(DomainGoodsInfo domainGoodsInfo) {
        mSearchResults = new MutableLiveData<>();
        mSearchResults.setValue(domainGoodsInfo);
    }

    @Override
    public void onSearchHistoryLoaded(DomainSearchItems domainSearchItems) {
        mSearchHistories = new MutableLiveData<>();
        mSearchHistories.setValue(domainSearchItems);
    }

    @Override
    public void onSearchSuggestionLoaded(DomainSearchItems domainSearchItems) {
        mSearchSuggestions = new MutableLiveData<>();
        mSearchSuggestions.setValue(domainSearchItems);
    }

    public LiveData<DomainGoodsInfo> getSearchResult(String key) {
        mSearchImpl.getSearchResult(key);
        return mSearchResults;
    }

    public LiveData<DomainSearchItems> getSearchHistories() {
        mSearchImpl.getSearchHistory();
        return mSearchHistories;
    }

    public LiveData<DomainSearchItems> getSearchSuggestion(String input) {
        mSearchImpl.getSearchSuggestion(input);
        return mSearchSuggestions;
    }

    @Override
    protected void onCleared() {
        mSearchImpl.unregisterCallback(this);
    }
}

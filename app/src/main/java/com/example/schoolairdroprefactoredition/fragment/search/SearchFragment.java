package com.example.schoolairdroprefactoredition.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.databinding.FragmentSearchBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.SearchSuggestionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SearchBar;
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;

public class SearchFragment extends Fragment implements SearchBar.OnSearchActionListener, EndlessRecyclerView.OnLoadMoreListener {
    private SearchViewModel searchViewModel;

    private SearchBar mSearchBar;
    private RecyclerView mHistory;
    private RecyclerView mSuggestion;
    private EndlessRecyclerView mResult;

    private HeaderFooterOnlyRecyclerAdapter mHistoryAdapter;
    private SearchSuggestionRecyclerAdapter mSuggestionAdapter;
    private HomeNearbyRecyclerAdapter mResultAdapter;

    private SearchHistoryHeader mHistoryHeader;

    private boolean isHistoryShowing = false;
    private boolean isSuggestionShowing = false;
    private boolean isResultShowing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        mSearchBar = binding.search;
        mHistory = binding.searchHistory;
        mSuggestion = binding.searchSuggestion;
        mResult = binding.searchResult;

        init();

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mHistoryAdapter = new HeaderFooterOnlyRecyclerAdapter();
        mSuggestionAdapter = new SearchSuggestionRecyclerAdapter();
        mResultAdapter = new HomeNearbyRecyclerAdapter();
        mHistoryHeader = new SearchHistoryHeader(getContext());
        mHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));
        mResult.setLayoutManager(new LinearLayoutManager(getContext()));

        mHistoryAdapter.addHeaderView(mHistoryHeader);

        mHistory.setAdapter(mHistoryAdapter);
        mSuggestion.setAdapter(mSuggestionAdapter);
        mResult.setAdapter(mResultAdapter);

        mSearchBar.setOnSearchActionListener(this);
        mHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    mSearchBar.closeSearch();
            }
        });
        mSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    mSearchBar.closeSearch();
            }
        });
        mResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    mSearchBar.closeSearch();
            }
        });
        mResult.setOnLoadMoreListener(this);
        showHistory();
        mSearchBar.openSearch();
    }

    private void showHistory() {
        isHistoryShowing = true;
        isSuggestionShowing = false;
        isResultShowing = false;
        mHistory.setVisibility(View.VISIBLE);
        mSuggestion.setVisibility(View.GONE);
        mResult.setVisibility(View.GONE);
    }

    private void showSuggestion() {
        isHistoryShowing = false;
        isSuggestionShowing = true;
        isResultShowing = false;
        mHistory.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.VISIBLE);
        mResult.setVisibility(View.GONE);
    }

    private void showResult() {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = true;
        mHistory.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.GONE);
        mResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        searchViewModel.getLastSearchedResult().observe(getViewLifecycleOwner(), data -> {
            mResultAdapter.addData(data);
            recycler.finishLoading();
        });
    }

    @Override
    public void onSearch(String key, View v) {
        showResult();
        mHistoryHeader.addHistory(key);
        searchViewModel.getSearchResult(key).observe(getViewLifecycleOwner(), data -> {
            mSearchBar.closeSearch();
            mResultAdapter.setList(data);
        });
    }

    @Override
    public void onInputChanged(String input) {
        if (input.trim().equals("")) {
            showHistory();
        } else
            showSuggestion();

        searchViewModel.getSearchSuggestion(input).observe(getViewLifecycleOwner(), data -> {
            // todo 将搜索建议显示在SearchSuggestion上
            mSuggestionAdapter.addData(data);
        });
    }

    @Override
    public void onFocusChanged(View v, boolean hasFocus) {
        if (hasFocus)
            showHistory();
    }

    @Override
    public void onCanceled() {
//        if (isResultShowing) {
//            showHistory();
//        } else {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
//        }
    }
}

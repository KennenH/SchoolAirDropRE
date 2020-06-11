package com.example.schoolairdroprefactoredition.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSearchBinding;
import com.example.schoolairdroprefactoredition.ui.components.SearchBar;

public class SearchFragment extends Fragment implements SearchBar.OnSearchActionListener, View.OnFocusChangeListener {
    private SearchViewModel searchViewModel;

    private SearchBar mSearchBar;
    private RecyclerView mHistory;
    private RecyclerView mSuggestion;
    private RecyclerView mResult;

    private boolean isHistoryShowing = false;
    private boolean isSuggestionShowing = false;
    private boolean isResultShowing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater, container, false);

        mSearchBar = binding.search;
        mHistory = binding.searchHistory;
        mSuggestion = binding.searchSuggestion;
        mResult = binding.searchResult;

        init();

        return binding.getRoot();
    }

    private void init() {
        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.setOnFocusChangeListener(this);
        mHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    hideKeyboard(recyclerView);
            }
        });
        mSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    hideKeyboard(recyclerView);
            }
        });
        mResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    hideKeyboard(recyclerView);
            }
        });


    }

    private void hideKeyboard(View view) {
        KeyboardUtils.hideSoftInput(view);
    }

    private void showKeyboard() {
        KeyboardUtils.showSoftInput();
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
    public void onSearch(String key, View v) {
        searchViewModel.getSearchResult(key).observe(getViewLifecycleOwner(), data -> {
            // todo 将搜索结果显示在SearchResult上
            hideKeyboard(v);
        });
    }

    @Override
    public void onInputChanged(String input) {
        searchViewModel.getSearchSuggestion(input).observe(getViewLifecycleOwner(), data -> {
            // todo 将搜索建议显示在SearchSuggestion上
        });
    }

    @Override
    public void onCanceled() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.search) {
            if (hasFocus)
                showKeyboard();
            else
                hideKeyboard(v);
        }
    }
}

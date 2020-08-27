package com.example.schoolairdroprefactoredition.fragment.search;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSearchPrelayoutBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.SearchSuggestionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SearchBar;
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class SearchFragment extends Fragment implements SearchBar.OnSearchActionListener, EndlessRecyclerView.OnLoadMoreListener {
    private SearchViewModel searchViewModel;

    private ConstraintSet constraintSet;
    private ConstraintLayout root;

    private SearchBar mSearchBar;
    private TextView mCancel;
    private SmartRefreshLayout mOverDrag;
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
        final FragmentSearchPrelayoutBinding binding = FragmentSearchPrelayoutBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        constraintSet = new ConstraintSet();
        root = binding.root;
        mSearchBar = binding.search;
        mCancel = binding.searchCancel;
        mOverDrag = binding.refresh;
        mHistory = binding.searchHistory;
        mSuggestion = binding.searchSuggestion;
        mResult = binding.searchResult;

        init();

        mOverDrag.setVisibility(View.INVISIBLE);
        root.post(this::startAnimation);

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
        mCancel.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });
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

    private void startAnimation() {
        constraintSet.clone(getContext(), R.layout.fragment_search);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new DecelerateInterpolator());
        transition.setDuration(100);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                mOverDrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(root, transition);
        constraintSet.applyTo(root);
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
}

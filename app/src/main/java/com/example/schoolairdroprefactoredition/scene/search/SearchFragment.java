package com.example.schoolairdroprefactoredition.scene.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
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
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.SearchSuggestionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.SearchBar;
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchBar.OnSearchActionListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener {
    private SearchViewModel searchViewModel;

    private ConstraintSet constraintSet;
    private ConstraintLayout root;

    private SearchBar mSearchBar;
    private TextView mCancel;
    private SmartRefreshLayout mOverDrag;

    private RecyclerView mHistory;
    private RecyclerView mSuggestion;
    private EndlessRecyclerView mResult;
    private LinearLayout mTip;
    private SpinKitView mLoading;

    private HeaderFooterOnlyRecyclerAdapter mHistoryAdapter;
    private SearchSuggestionRecyclerAdapter mSuggestionAdapter;
    private HomeNearbyRecyclerAdapter mResultAdapter;

    private SearchHistoryHeader mHistoryHeader;

    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;
    private double longitude;
    private double latitude;

    private boolean isHistoryShowing = false;
    private boolean isSuggestionShowing = false;
    private boolean isResultShowing = false;

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
            try {
                longitude = (double) bundle.getSerializable(ConstantUtil.LONGITUDE);
                latitude = (double) bundle.getSerializable(ConstantUtil.LATITUDE);
            } catch (NullPointerException ignored) {
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentSearchPrelayoutBinding binding = FragmentSearchPrelayoutBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setOnRequestListener(this);

        constraintSet = new ConstraintSet();
        root = binding.root;
        mSearchBar = binding.search;
        mCancel = binding.searchCancel;
        mOverDrag = binding.refresh;
        mHistory = binding.searchHistory;
        mSuggestion = binding.searchSuggestion;
        mResult = binding.searchResult;
        mTip = binding.searchTip;
        mLoading = binding.loading;

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
        mResultAdapter = new HomeNearbyRecyclerAdapter(info);
        mHistoryHeader = new SearchHistoryHeader(getContext());
        mHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));
        mResult.setLayoutManager(new LinearLayoutManager(getContext()));

        mHistoryAdapter.addHeaderView(mHistoryHeader);
        mHistory.setAdapter(mHistoryAdapter);
        mSuggestion.setAdapter(mSuggestionAdapter);
        mResult.setAdapter(mResultAdapter);

        searchViewModel.getSearchHistories().observe(getViewLifecycleOwner(), histories -> mHistoryHeader.showAfterUpdate(histories.getHistoryList()));

        mSearchBar.setOnSearchActionListener(this);
        mCancel.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });
        mHistoryHeader.setOnHistoryActionListener(new SearchHistoryHeader.OnHistoryActionListener() {
            @Override
            public void onDeleteHistory() {
                searchViewModel.deleteHistories();
                mHistoryHeader.showAfterUpdate(new ArrayList<>());
            }

            @Override
            public void onSearchHistory(String key) {
                mSearchBar.setInputKey(key);
                performSearch(key);
            }
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

    /**
     * 执行搜索
     * 显示结果列表
     *
     * @param key 搜索关键词
     */
    private void performSearch(String key) {
        if (token != null) {
            showLoading();
            searchViewModel.getSearchResult(token.getAccess_token(), longitude, latitude, key).observe(getViewLifecycleOwner(), data -> {
                mSearchBar.closeSearch();
                mResultAdapter.setList(data);

                showResult();
                if (data.size() < 1) {
                    showTip();
                }
            });
            searchViewModel.getSearchHistories().observe(getViewLifecycleOwner(), histories -> mHistoryHeader.showAfterUpdate(histories.getHistoryList()));
        }
    }

    private void startAnimation() {
        constraintSet.clone(getContext(), R.layout.fragment_search);
        Transition transition = new ChangeBounds();
        transition.setInterpolator(new DecelerateInterpolator());
        transition.setDuration(150);
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
        mSuggestion.setVisibility(View.GONE);
        mResult.setVisibility(View.GONE);
        mTip.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mHistory.setVisibility(View.VISIBLE);
    }

    private void showSuggestion() {
        isHistoryShowing = false;
        isSuggestionShowing = true;
        isResultShowing = false;
        mHistory.setVisibility(View.GONE);
        mResult.setVisibility(View.GONE);
        mTip.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = true;
        mHistory.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.GONE);
        mTip.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mResult.setVisibility(View.VISIBLE);
    }

    private void showTip() {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = false;
        mHistory.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.GONE);
        mResult.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mTip.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = false;
        mHistory.setVisibility(View.GONE);
        mSuggestion.setVisibility(View.GONE);
        mResult.setVisibility(View.GONE);
        mTip.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        if (token != null)
            searchViewModel.getLastSearchedResult(token.getAccess_token(), longitude, latitude).observe(getViewLifecycleOwner(), data -> {
                mResultAdapter.addData(data);
                recycler.finishLoading();
            });
    }

    @Override
    public void onSearch(String key, View v) {
        performSearch(key);
    }

    @Override
    public void onInputChanged(String input) {
        if (input.trim().equals("")) {
            showHistory();
        } else
            showSuggestion();
    }

    @Override
    public void onFocusChanged(View v, boolean hasFocus) {
        if (hasFocus)
            showHistory();
    }

    @Override
    public void onError() {
        mLoading.setVisibility(View.GONE);
    }

}

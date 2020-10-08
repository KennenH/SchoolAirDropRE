package com.example.schoolairdroprefactoredition.scene.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.SearchSuggestionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.SearchBar;
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchBar.OnSearchActionListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener, BaseFooterAdapter.OnNoMoreDataListener {
    private SearchViewModel searchViewModel;

    private FragmentSearchPrelayoutBinding binding;
    private ConstraintSet constraintSet = new ConstraintSet();

    private HeaderFooterOnlyRecyclerAdapter mHistoryAdapter;
    private SearchSuggestionRecyclerAdapter mSuggestionAdapter;
    private HomeNearbyRecyclerAdapter mResultAdapter;

    private SearchHistoryHeader mHistoryHeader;

    private Bundle bundle;
    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;
    private double longitude;
    private double latitude;

    private boolean isHistoryShowing = false;
    private boolean isSuggestionShowing = false;
    private boolean isResultShowing = false;

    private int searchPage = 1;

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
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
        binding = FragmentSearchPrelayoutBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setOnRequestListener(this);

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
        mResultAdapter.setOnNoMoreDataListener(this);
        mHistoryHeader = new SearchHistoryHeader(getContext());
        binding.searchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.searchSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.searchResult.setLayoutManager(new LinearLayoutManager(getContext()));

        mHistoryAdapter.addHeaderView(mHistoryHeader);
        binding.searchHistory.setAdapter(mHistoryAdapter);
        binding.searchSuggestion.setAdapter(mSuggestionAdapter);
        binding.searchResult.setAdapter(mResultAdapter);

        searchViewModel.getSearchHistories().observe(getViewLifecycleOwner(), histories -> mHistoryHeader.showAfterUpdate(histories.getHistoryList()));

        binding.search.setOnSearchActionListener(this);
        binding.searchCancel.setOnClickListener(v -> {
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
                binding.search.setInputKey(key);
                performSearch(key);
            }
        });
        binding.searchHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    binding.search.closeSearch();
            }
        });
        binding.searchSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    binding.search.closeSearch();
            }
        });
        binding.searchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    binding.search.closeSearch();
            }
        });

//        showHistory();
        binding.searchResult.setOnLoadMoreListener(this);
        binding.search.openSearch();
        binding.refresh.setVisibility(View.INVISIBLE);
        binding.root.post(this::startAnimation);
    }

    /**
     * 执行搜索
     * 显示结果列表
     *
     * @param key 搜索关键词
     */
    private void performSearch(String key) {
        if (!key.trim().equals("")) {
            binding.search.closeSearch();
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            searchViewModel.getSearchResult(longitude, latitude, key).observe(getViewLifecycleOwner(), data -> {
                if (data.size() < 1) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_SEARCH);
                } else {
                    mResultAdapter.setList(data);
                    showResult();
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
                binding.refresh.setVisibility(View.VISIBLE);
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
        TransitionManager.beginDelayedTransition(binding.root, transition);
        constraintSet.applyTo(binding.root);
    }

    private void showHistory() {
        isHistoryShowing = true;
        isSuggestionShowing = false;
        isResultShowing = false;
        binding.searchSuggestion.setVisibility(View.GONE);
        binding.searchResult.setVisibility(View.GONE);
        binding.searchHistory.setVisibility(View.VISIBLE);
        binding.searchPlaceHolder.setVisibility(View.GONE);
    }

    private void showSuggestion() {
        isHistoryShowing = false;
        isSuggestionShowing = true;
        isResultShowing = false;
        binding.searchHistory.setVisibility(View.GONE);
        binding.searchResult.setVisibility(View.GONE);
        binding.searchSuggestion.setVisibility(View.VISIBLE);
        binding.searchPlaceHolder.setVisibility(View.GONE);
    }

    private void showResult() {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = true;
        binding.searchHistory.setVisibility(View.GONE);
        binding.searchSuggestion.setVisibility(View.GONE);
        binding.searchResult.setVisibility(View.VISIBLE);
        binding.searchPlaceHolder.setVisibility(View.GONE);
    }

    private void showPlaceHolder(int type) {
        isHistoryShowing = false;
        isSuggestionShowing = false;
        isResultShowing = false;
        binding.searchHistory.setVisibility(View.GONE);
        binding.searchSuggestion.setVisibility(View.GONE);
        binding.searchResult.setVisibility(View.GONE);
        binding.searchPlaceHolder.setVisibility(View.VISIBLE);
        binding.searchPlaceHolder.setPlaceHolderType(type);
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
//        if (token != null)
//            searchViewModel.getSearchResult().observe(getViewLifecycleOwner(), data -> {
//                mResultAdapter.addData(data);
//                recycler.finishLoading();
//            });
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
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onNoMoreData() {
        binding.searchResult.setIsNoMoreData(true);
    }

    @Override
    public void onNoMoreDataRefresh() {
        binding.searchResult.setIsNoMoreData(false);
    }
}

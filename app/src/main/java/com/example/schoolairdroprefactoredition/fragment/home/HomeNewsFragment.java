package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeNewsFragment extends BaseChildFragment implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseParentFragment.OnLocationCallbackListener, BaseChildFragmentViewModel.OnRequestListener {
    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;

    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

    private AMapLocation mLocation;

    private int mFragmentNum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentNum = getArguments() != null ? getArguments().getInt(ConstantUtil.FRAGMENT_NUM) : 0;

        if (getParentFragment() instanceof BaseParentFragment)
            ((BaseParentFragment) getParentFragment()).setOnLocationCallbackListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        initRecycler();

        if (mLocation == null)
            locate();// 请求MainActivity的定位

        return binding.getRoot();
    }

    private void initRecycler() {
        mRefresh.setOnRefreshListener(this);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mEndlessRecyclerView.setLayoutManager(manager);
        mEndlessRecyclerView.addItemDecoration(new MarginItemDecoration());
        mHomeNewsRecyclerAdapter = new HomeNewsRecyclerAdapter();
        mEndlessRecyclerView.setAdapter(mHomeNewsRecyclerAdapter);

        mEndlessRecyclerView.post(() -> {
            // 刷新布局管理器和item decoration 解决错误的spacing
            manager.invalidateSpanAssignments();
            mEndlessRecyclerView.invalidateItemDecorations();
        });

        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> mHomeNewsRecyclerAdapter.setList(data));
    }

    /**
     * 来自父Fragment{@link ParentNewsFragment}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;

                // 定位成功时才设置观察者
                homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                    showContentContainer();
                    mHomeNewsRecyclerAdapter.setList(data);
                });
            } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) {
            homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                mHomeNewsRecyclerAdapter.setList(data);
                showContentContainer();
                refreshLayout.finishRefresh();
            });
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.addData(data);
            showContentContainer();
            recycler.finishLoading();
        });
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onLoading() {
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
    }
}

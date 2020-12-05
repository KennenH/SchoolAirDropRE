package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class HomePurchasingFragment extends BaseChildFragment
        implements BaseStateViewModel.OnRequestListener, BaseFooterAdapter.OnNoMoreDataListener {
    private HomePurchasingFragmentViewModel homeContentFragmentViewModel;

    private EndlessRecyclerView mEndlessRecyclerView;

    private HomeNearbyRecyclerAdapter mHomeNearbyRecyclerAdapter;

    private LinearLayoutManager mManager;


    public static HomePurchasingFragment newInstance() {
        return new HomePurchasingFragment();
    }

    @Override
    public void initView(FragmentHomeContentBinding binding) {
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomePurchasingFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        SmartRefreshLayout mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        mRefresh.setOnRefreshListener(this);
        mManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mEndlessRecyclerView.setLayoutManager(mManager);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        mHomeNearbyRecyclerAdapter = new HomeNearbyRecyclerAdapter();
        mHomeNearbyRecyclerAdapter.setOnNoMoreDataListener(this);
        mEndlessRecyclerView.setAdapter(mHomeNearbyRecyclerAdapter);
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     */
    public void scrollToTop() {
        if (mManager.findLastVisibleItemPosition() > 10) {
            mEndlessRecyclerView.scrollToPosition(5);
        }
        mEndlessRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * 没有更多数据
     */
    @Override
    public void onNoMoreData() {
        mEndlessRecyclerView.setIsNoMoreData(true);
    }

    /**
     * 刷新列表后
     */
    @Override
    public void onNoMoreDataRefresh() {
        mEndlessRecyclerView.setIsNoMoreData(false);
    }

    @Override
    public void getOnlineData(AMapLocation aMapLocation) {
        homeContentFragmentViewModel.getGoodsInfo(aMapLocation.getLongitude(), aMapLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
            if (data.size() == 0)
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_GOODS);
            else {
                mHomeNearbyRecyclerAdapter.setList(data);
                showContentContainer();
            }
        });
    }

    @Override
    public void getRefreshData(@NonNull RefreshLayout refreshLayout, AMapLocation aMapLocation) {
        homeContentFragmentViewModel.getGoodsInfo(aMapLocation.getLongitude(), aMapLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
            refreshLayout.finishRefresh();
            if (data.size() == 0) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_GOODS);
            } else {
                mHomeNearbyRecyclerAdapter.setList(data);
                showContentContainer();
            }
        });
    }

    @Override
    public void getAutoLoadMoreData(AMapLocation aMapLocation) {
        //            homeContentFragmentViewModel.getGoodsInfo().observe(getViewLifecycleOwner(), data -> {
//                recycler.finishLoading();
//                mHomeNearbyRecyclerAdapter.addData(data);
//                showContentContainer();
//            });
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }
}

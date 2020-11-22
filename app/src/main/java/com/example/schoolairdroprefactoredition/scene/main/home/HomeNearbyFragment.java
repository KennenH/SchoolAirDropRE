package com.example.schoolairdroprefactoredition.scene.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class HomeNearbyFragment extends BaseChildFragment
        implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener, MainActivity.OnLocationListener, BaseFooterAdapter.OnNoMoreDataListener {
    private HomeNearbyFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;
    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNearbyRecyclerAdapter mHomeNearbyRecyclerAdapter;

    private AMapLocation mLocation = null;

    public static HomeNearbyFragment newInstance() {
        return new HomeNearbyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).addOnLocationListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNearbyFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        initRecycler();

        return binding.getRoot();
    }

    private void initRecycler() {
        mRefresh.setOnRefreshListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mEndlessRecyclerView.setLayoutManager(manager);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        mHomeNearbyRecyclerAdapter = new HomeNearbyRecyclerAdapter();
        mHomeNearbyRecyclerAdapter.setOnNoMoreDataListener(this);
        mEndlessRecyclerView.setAdapter(mHomeNearbyRecyclerAdapter);
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

    /**
     * 来自 {@link MainActivity}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;
                homeContentFragmentViewModel.getGoodsInfo(mLocation.getLongitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                    if (data.size() == 0)
                        showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_GOODS_HOME);
                    else {
                        mHomeNearbyRecyclerAdapter.setList(data);
                        showContentContainer();
                    }
                });
            } else {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
            }
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
        }
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) { // 定位信息不为null时
            homeContentFragmentViewModel.getGoodsInfo(mLocation.getLongitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                refreshLayout.finishRefresh();
                if (data.size() == 0) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_GOODS_HOME);
                } else {
                    mHomeNearbyRecyclerAdapter.setList(data);
                    showContentContainer();
                }
            });
        } else { // 定位失败时通知父Fragment显示PlaceHolder
            refreshLayout.finishRefresh();
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
            locateWithoutRequest();// 自动请求MainActivity的定位
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
//        if (mLocation != null) {
//            homeContentFragmentViewModel.getGoodsInfo().observe(getViewLifecycleOwner(), data -> {
//                recycler.finishLoading();
//                mHomeNearbyRecyclerAdapter.addData(data);
//                showContentContainer();
//            });
//        } else {
//            showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
//            recycler.finishLoading();
//        }
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }
}

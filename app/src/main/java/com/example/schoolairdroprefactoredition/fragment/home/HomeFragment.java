package com.example.schoolairdroprefactoredition.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.AMapActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.ui.components.Location;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.lucode.hackware.magicindicator.MagicIndicator;

public class HomeFragment extends Fragment
        implements View.OnClickListener, HomeRecycler.OnLoadMoreListener, OnRefreshListener, AMapLocationListener {

    private HomeViewModel homeViewModel;

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Location mLocation;

    private MagicIndicator mIndicator;
    private ViewPager mViewPager;

    private SmartRefreshLayout mRefreshLayout;
    private HomeRecycler mRecycler;
    private HomeRecycler.HomeRecyclerAdapter mAdapter;

    private int locateTimes = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        mIndicator = binding.homeIndicator;
        mViewPager = binding.homeViewpager;

        mRefreshLayout = binding.homeRefresh;
        mLocation = binding.homeLocation;
        mRecycler = binding.homeRecycler;

        mLocationClient = new AMapLocationClient(getContext());
        mLocationOption = new AMapLocationClientOption();
        mAdapter = new HomeRecycler.HomeRecyclerAdapter();

        mRefreshLayout.setOnRefreshListener(this);
        mLocation.setOnClickListener(this);
        binding.homeTapToTop.setOnClickListener(this);
        binding.homeSearchBar.setOnClickListener(this);

        initRecyclerOnlineData();
        initRecycler();
        initLocation();

        return binding.getRoot();
    }

    private void initLocation() {
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(this);
        // 注意合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        // 启动定位
        mLocationClient.startLocation();
        mLocation.setLocation("正在定位");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation.setLocation(aMapLocation.getCity());
            } else {
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                if (locateTimes < 3) {
                    mLocation.setLocation("正在定位");
                } else {
                    mLocation.setLocation("定位失败");
                    mLocationClient.stopLocation();
                }
                locateTimes++;
            }
        }
    }

    private void initRecyclerOnlineData() {
        homeViewModel.getRecyclerData().observe(getViewLifecycleOwner(), data -> {
            mAdapter.setList(data);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ConstraintLayout header = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.component_recycler_header, mRecycler, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(params);
        mAdapter.addHeaderView(header);
        mRecycler.addItemDecoration(new MarginItemDecoration());
        mRecycler.setOnLoadMoreListener(this);
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_tap_to_top) {
            mRecycler.smoothScrollToPosition(0);
        } else if (id == R.id.home_search_bar) {
            // todo open search activity

        } else if (id == R.id.home_location) {
            // open map
            Intent intent = new Intent(getContext(), AMapActivity.class);
            if (getContext() != null)
                getContext().startActivity(intent);
        }
    }

    @Override
    public void autoLoadMore(HomeRecycler recycler) {
        homeViewModel.getRecyclerData().observe(getViewLifecycleOwner(), data -> {
            mAdapter.addData(data);
            recycler.finishLoading();
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeViewModel.getRecyclerData().observe(getViewLifecycleOwner(), data -> {
            mAdapter.setList(data);
            mAdapter.notifyDataSetChanged();
            mRecycler.finishLoading();
            refreshLayout.finishRefresh();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

}

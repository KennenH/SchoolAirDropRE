package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.fragment.purchasing.PurchasingFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeNearbyFragment extends Fragment
        implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, PurchasingFragment.OnLocationCallbackListener {
    private int mFragmentNum;

    private HomeNearbyFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;

    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNearbyRecyclerAdapter mHomeNearbyRecyclerAdapter;

    private AMapLocation mLocation;

    private OnLocationCalledBackListener mOnLocationCalledBackListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentNum = getArguments() != null ? getArguments().getInt(ConstantUtil.FRAGMENT_NUM) : 0;
        if (getParentFragment() != null && getParentFragment() instanceof PurchasingFragment)
            ((PurchasingFragment) getParentFragment()).setOnLocationCallbackListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNearbyFragmentViewModel.class);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        initRecycler();

        return binding.getRoot();
    }

    private void initRecycler() {
        mRefresh.setOnRefreshListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, mFragmentNum == ConstantUtil.FRAGMENT_NUM_NEWS);
        mEndlessRecyclerView.setLayoutManager(manager);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        mHomeNearbyRecyclerAdapter = new HomeNearbyRecyclerAdapter();
        mEndlessRecyclerView.setAdapter(mHomeNearbyRecyclerAdapter);
    }

    /**
     * 当从父Fragment {@link PurchasingFragment}获取定位回调时
     * 通知回父Fragment以更新placeholder状态
     */
    public interface OnLocationCalledBackListener {
        void onLocationSuccess();

        void onLocationFailed();
    }

    public void setOnLocationCalledBackListener(OnLocationCalledBackListener listener) {
        mOnLocationCalledBackListener = listener;
    }

    /**
     * 来自父Fragment {@link PurchasingFragment}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;
                if (mOnLocationCalledBackListener != null)
                    mOnLocationCalledBackListener.onLocationSuccess();

                // 定位回调成功以后才为网络api设置观察者
                homeContentFragmentViewModel.getGoodsInfo(aMapLocation.getLongitude(), aMapLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                    mHomeNearbyRecyclerAdapter.setList(data);
                });
            } else {
                if (mOnLocationCalledBackListener != null)
                    mOnLocationCalledBackListener.onLocationFailed();
            }
        } else {
            if (mOnLocationCalledBackListener != null)
                mOnLocationCalledBackListener.onLocationFailed();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null)
            homeContentFragmentViewModel.getGoodsInfo(mLocation.getLatitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                mHomeNearbyRecyclerAdapter.setList(data);
                refreshLayout.finishRefresh();
            });
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        if (mLocation != null)
            homeContentFragmentViewModel.getGoodsInfo(mLocation.getLongitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                mHomeNearbyRecyclerAdapter.addData(data);
                recycler.finishLoading();
            });

    }
}

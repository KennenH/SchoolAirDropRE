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
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNearbyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class HomeNearbyFragment extends BaseChildFragment
        implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener, MainActivity.OnLocationListener {
    private HomeNearbyFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;
    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNearbyRecyclerAdapter mHomeNearbyRecyclerAdapter;

    private AMapLocation mLocation = null;

    private int mFragmentNum;
    private Bundle bundle;
    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;

    public static HomeNearbyFragment newInstance(Bundle bundle) {
        HomeNearbyFragment fragment = new HomeNearbyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        mFragmentNum = bundle.getInt(ConstantUtil.FRAGMENT_NUM, 0);
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(this);
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
        if (mLocation == null)
            locateWithoutRequest();// 自动请求MainActivity的定位

        return binding.getRoot();
    }

    private void initRecycler() {
        mRefresh.setOnRefreshListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, mFragmentNum == ConstantUtil.FRAGMENT_NUM_NEWS);
        mEndlessRecyclerView.setLayoutManager(manager);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        mHomeNearbyRecyclerAdapter = new HomeNearbyRecyclerAdapter(bundle);
        mEndlessRecyclerView.setAdapter(mHomeNearbyRecyclerAdapter);
    }

    /**
     * 来自父Fragment {@link ParentPurchasingFragment}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;

                if (token != null) {
                    homeContentFragmentViewModel.getGoodsInfo(token.getAccess_token(), 1, aMapLocation.getLongitude(), aMapLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                        if (data.size() == 0) {
                            showPlaceHolder(StatePlaceHolder.TYPE_EMPTY);
                        } else {
                            mHomeNearbyRecyclerAdapter.setList(data);
                            showContentContainer();
                        }
                    });
                } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
            } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) { // 定位信息不为null时
            if (token != null)
                homeContentFragmentViewModel.getGoodsInfo(token.getAccess_token(), 1, mLocation.getLongitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
                    refreshLayout.finishRefresh();
                    if (data.size() == 0) {
                        showPlaceHolder(StatePlaceHolder.TYPE_EMPTY);
                    } else {
                        mHomeNearbyRecyclerAdapter.setList(data);
                        showContentContainer();
                    }
                });
            else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else { // 定位失败时通知父Fragment显示PlaceHolder
            refreshLayout.finishRefresh();
            showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
            locateWithoutRequest();// 自动请求MainActivity的定位
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
//        if (mLocation != null) {
//            if (token != null)
//                homeContentFragmentViewModel.getGoodsInfo(token.getAccess_token(), 1, mLocation.getLongitude(), mLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
//                    mHomeNearbyRecyclerAdapter.addData(data);
//                    showContentContainer();
//                    recycler.finishLoading();
//                });
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

package com.example.schoolairdroprefactoredition.scene.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

public class HomeNewsFragment extends BaseChildFragment implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener, MainActivity.OnLoginStateChangedListener, MainActivity.OnLocationListener, HomeNewsRecyclerAdapter.OnNoMoreDataListener {
    private FragmentHomeContentBinding binding;
    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

    private StaggeredGridLayoutManager mManager;

    private AMapLocation mLocation;

    private static boolean requested = false; // 只在第一个新闻页加载时询问权限

    public static HomeNewsFragment newInstance() {
        return new HomeNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        initRecycler();

        if (mLocation == null && !requested) {
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            locate(PermissionBaseActivity.RequestType.AUTO);// 自动请求MainActivity的定位}
            requested = true;
        }

        return binding.getRoot();
    }

    private void initRecycler() {
        binding.homeRefresh.setOnRefreshListener(this);
        binding.homeRecycler.setOnLoadMoreListener(this);

        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        binding.homeRecycler.setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f));
        binding.homeRecycler.setLayoutManager(mManager);
        binding.homeRecycler.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(1f), true));
        mHomeNewsRecyclerAdapter = new HomeNewsRecyclerAdapter();
        mHomeNewsRecyclerAdapter.setOnNoMoreDataListener(this);
        binding.homeRecycler.setAdapter(mHomeNewsRecyclerAdapter);
    }

    /**
     * 来自{@link MainActivity}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;
                homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                    if (data.size() < 1) {
                        showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_NEWS_HOME);
                    } else {
                        mHomeNewsRecyclerAdapter.setList(data);
                        showContentContainer();
                    }
                });
            } else showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
        } else showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) {
            homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                refreshLayout.finishRefresh();
                if (data.size() < 1) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_NEWS_HOME);
                } else {
                    mHomeNewsRecyclerAdapter.setList(data);
                    showContentContainer();
                }
            });
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
//        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
//            mHomeNewsRecyclerAdapter.addData(data);
//            showContentContainer();
//            recycler.finishLoading();
//        });
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onLoginStateChanged(@NotNull Bundle bundle) {

    }

    @Override
    public void onNoMoreData() {
        binding.homeRecycler.setIsNoMoreData(true);
    }

    @Override
    public void onNoMoreDataRefresh() {
        binding.homeRecycler.setIsNoMoreData(false);
    }
}

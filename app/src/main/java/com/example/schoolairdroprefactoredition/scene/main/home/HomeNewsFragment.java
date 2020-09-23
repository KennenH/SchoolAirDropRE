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
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

public class HomeNewsFragment extends BaseChildFragment implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseStateViewModel.OnRequestListener, MainActivity.OnLoginStateChangedListener, MainActivity.OnLocationListener {
    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;
    private EndlessRecyclerView mEndlessRecyclerView;

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
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        initRecycler();

        if (mLocation == null && !requested) {
            locate(PermissionBaseActivity.RequestType.AUTO);// 自动请求MainActivity的定位}
            requested = true;
        }

        return binding.getRoot();
    }

    private void initRecycler() {
        mRefresh.setOnRefreshListener(this);
        mEndlessRecyclerView.setOnLoadMoreListener(this);

        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mEndlessRecyclerView.setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f));
        mEndlessRecyclerView.setLayoutManager(mManager);
        mEndlessRecyclerView.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(1f),true));
        mHomeNewsRecyclerAdapter = new HomeNewsRecyclerAdapter();
        mEndlessRecyclerView.setAdapter(mHomeNewsRecyclerAdapter);

        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.setList(data);
        });
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
//                        if (data == null || data.size() == 0) {
//                            showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME);
//                        } else {
                    mHomeNewsRecyclerAdapter.setList(data);
                    showContentContainer();
//                        }
                });
            } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
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
        showPlaceHolder(StatePlaceHolder.TYPE_UNKNOWN);
    }

    @Override
    public void onLoginStateChanged(@NotNull Bundle bundle) {

    }
}

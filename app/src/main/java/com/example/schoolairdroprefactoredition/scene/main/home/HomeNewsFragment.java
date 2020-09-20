package com.example.schoolairdroprefactoredition.scene.main.home;

import android.content.Context;
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
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
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

    private int mFragmentNum;

    private static boolean requested = false; // 只在第一个新闻页加载时询问权限

    public static HomeNewsFragment newInstance(Bundle bundle) {
        HomeNewsFragment fragment = new HomeNewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentNum = getArguments() != null ? getArguments().getInt(ConstantUtil.FRAGMENT_NUM) : 0;

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setOnLocationListener(this);
        }
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
        mEndlessRecyclerView.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(1f)));
        mHomeNewsRecyclerAdapter = new HomeNewsRecyclerAdapter();
        mEndlessRecyclerView.setAdapter(mHomeNewsRecyclerAdapter);

        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.setList(data);
        });
    }

    private void invalidateDecoration() {
        mManager.invalidateSpanAssignments();
        mEndlessRecyclerView.invalidateItemDecorations();
        mHomeNewsRecyclerAdapter.notifyDataSetChanged();
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
                    invalidateDecoration();
                });
            } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) {
            homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                mHomeNewsRecyclerAdapter.setList(data);
                invalidateDecoration();
                showContentContainer();
                refreshLayout.finishRefresh();
            });
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.addData(data);
            invalidateDecoration();
            showContentContainer();
            recycler.finishLoading();
        });
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onLoginStateChanged(@NotNull Bundle bundle) {

    }
}

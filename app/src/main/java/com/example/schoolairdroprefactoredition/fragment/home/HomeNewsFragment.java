package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.fragment.BaseMainFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeNewsFragment extends Fragment implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener, BaseMainFragment.OnLocationCallbackListener {
    private int mFragmentNum;

    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;

    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

    private AMapLocation mLocation;

    public static HomeNewsFragment newInstance(int args) {
        HomeNewsFragment fragment = new HomeNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtil.FRAGMENT_NUM, args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentNum = getArguments() != null ? getArguments().getInt(ConstantUtil.FRAGMENT_NUM) : 0;
        if (getParentFragment() != null && getParentFragment() instanceof HomeFragment)
            ((HomeFragment) getParentFragment()).setOnLocationCallbackListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        initRecycler();

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
        // 刷新布局管理器和item decoration 解决错误的spacing
        mEndlessRecyclerView.post(() -> {
            manager.invalidateSpanAssignments();
            mEndlessRecyclerView.invalidateItemDecorations();
        });

        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> mHomeNewsRecyclerAdapter.setList(data));
    }

    /**
     * 来自父Fragment{@link HomeFragment}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;
//                Log.d("NewsFragment", aMapLocation.getAddress());
            } else {
//                Log.d("NewsFragment", "onLocation error -- > " + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.setList(data);
            refreshLayout.finishRefresh();
        });
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            mHomeNewsRecyclerAdapter.addData(data);
            recycler.finishLoading();
        });
    }
}

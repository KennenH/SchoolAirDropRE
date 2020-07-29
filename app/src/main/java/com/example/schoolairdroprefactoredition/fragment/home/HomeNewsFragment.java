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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.example.schoolairdroprefactoredition.utils.decoration.VerticalItemLineDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeNewsFragment extends Fragment implements OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener {
    private int mFragmentNum;

    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;

    private EndlessRecyclerView mEndlessRecyclerView;
    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);

        mRefresh = binding.homeRefresh;
        mEndlessRecyclerView = binding.homeRecycler;

        init();

        return binding.getRoot();
    }

    private void init() {
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

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

import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeNearbyFragment extends Fragment implements OnRefreshListener, HomeRecycler.OnLoadMoreListener {
    private int mFragmentNum;

    private HomeNearbyFragmentViewModel homeContentFragmentViewModel;

    private SmartRefreshLayout mRefresh;

    private HomeRecycler mHomeRecycler;
    private HomeRecycler.HomeNearbyRecyclerAdapter mHomeNearbyRecyclerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentNum = getArguments() != null ? getArguments().getInt(ConstantUtil.FRAGMENT_NUM) : 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNearbyFragmentViewModel.class);

        mRefresh = binding.homeRefresh;
        mHomeRecycler = binding.homeRecycler;

        init();

        return binding.getRoot();
    }

    private void init() {
        mRefresh.setOnRefreshListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, mFragmentNum == ConstantUtil.FRAGMENT_NUM_NEWS);
        mHomeRecycler.setLayoutManager(manager);
        mHomeRecycler.setOnLoadMoreListener(this);

        mHomeNearbyRecyclerAdapter = new HomeRecycler.HomeNearbyRecyclerAdapter();
        mHomeRecycler.setAdapter(mHomeNearbyRecyclerAdapter);

        homeContentFragmentViewModel.getGoodsInfo().observe(getViewLifecycleOwner(), data -> {
            mHomeNearbyRecyclerAdapter.addData(data);
        });
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeContentFragmentViewModel.getGoodsInfo().observe(getViewLifecycleOwner(), data -> {
            mHomeNearbyRecyclerAdapter.setList(data);
            refreshLayout.finishRefresh();
        });
    }

    @Override
    public void autoLoadMore(HomeRecycler recycler) {
        homeContentFragmentViewModel.getGoodsInfo().observe(getViewLifecycleOwner(), data -> {
            mHomeNearbyRecyclerAdapter.addData(data);
            recycler.finishLoading();
        });
    }
}

package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

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

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class SSBBaseFragment extends Fragment implements BaseStateViewModel.OnRequestListener, SSBFilter.OnFilterListener {

    protected SSBViewModel viewModel;

    protected RecyclerView mRecycler;
    protected SSBAdapter mAdapter;
    protected SSBFilter mFilter;
    protected SpinKitView mLoading;

    protected List<DomainGoodsInfo.DataBean> mList;

    protected Bundle bundle;

    protected DomainAuthorize token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding
                = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SSBViewModel.class);
        viewModel.setOnRequestListener(this);

        mLoading = binding.loading;
        mRecycler = binding.ssbRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter();

        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        mRecycler.setAdapter(mAdapter);

        init(binding);

        return binding.getRoot();
    }

    protected void init(FragmentSsbBinding binding) {

    }

    @Override
    public void onError() {
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFilterTimeAsc() {

    }

    @Override
    public void onFilterTimeDesc() {

    }

    @Override
    public void onFilterWatches() {

    }
}

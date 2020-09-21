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
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.util.List;

public class SSBBaseFragment extends Fragment implements BaseStateViewModel.OnRequestListener, SSBFilter.OnFilterListener {

    protected SSBViewModel viewModel;

    protected SSBAdapter mAdapter;
    protected SSBFilter mFilter;
    protected LoadingPopupView mLoading;

    protected List<DomainGoodsInfo.DataBean> mList;

    protected Bundle bundle;

    protected DomainUserInfo.DataBean info;
    protected DomainAuthorize token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding
                = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SSBViewModel.class);
        viewModel.setOnRequestListener(this);

        binding.ssbRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mLoading = MyUtil.loading(getContext());
        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter(bundle);

        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        binding.ssbRecycler.setAdapter(mAdapter);

        init(binding);

        return binding.getRoot();
    }

    protected void showLoading() {
        if (mLoading == null)
            mLoading = MyUtil.loading(getContext());
        mLoading.show();
    }

    protected void dismissLoading() {
        if (mLoading != null)
            mLoading.dismiss();
    }

    /**
     * 子fragment初始化方法
     */
    protected void init(FragmentSsbBinding binding) {
    }

    @Override
    public void onError() {
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

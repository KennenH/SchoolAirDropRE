package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;

import java.util.List;

public abstract class SSBBaseFragment extends StatePlaceholderFragment
        implements BaseStateViewModel.OnRequestListener, SSBFilter.OnFilterListener,
        StatePlaceHolder.OnPlaceHolderRefreshListener, SSBViewModel.OnSSBActionListener,
        SSBAdapter.OnSSBItemActionListener{

    protected SSBViewModel viewModel;

    protected SSBAdapter mAdapter;
    protected SSBFilter mFilter;

    protected com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding;
    protected List<DomainGoodsInfo.DataBean> mList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SSBViewModel.class);
        viewModel.setOnRequestListener(this);
        viewModel.setOnSSBActionListener(this);

        binding.ssbRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.placeHolder.setOnPlaceHolderActionListener(this);

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter();
        mAdapter.setOnSSBItemActionListener(this);
        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        binding.ssbRecycler.setAdapter(mAdapter);
        binding.ssbRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity)
                    ((SSBActivity) getActivity()).hideSearchBar();
            }
        });
        init(binding);
        return binding.getRoot();
    }

    @Override
    public void setContainerAndPlaceholder() {
        mStatePlaceholderFragmentContainer = binding.ssbRecycler;
        mStatePlaceholderFragmentPlaceholder = binding.placeHolder;
    }

    /**
     * 子fragment在获取网络数据之后调用
     */
    protected void loadData(DomainGoodsInfo data) {
        mList = data.getData();
        if (mList.size() == 0)
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
        else {
            mAdapter.setList(mList);
            showContentContainer();
        }
    }

    /**
     * 获取{@link SSBActivity}的登录信息
     */
    protected DomainAuthorize getToken() {
        try {
            return (DomainAuthorize) getActivity().getIntent().getExtras().getSerializable(ConstantUtil.KEY_AUTHORIZE);
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    /**
     * 初始化
     */
    public abstract void init(FragmentSsbBinding binding);

    /**
     * 重试网络数据获取
     */
    public abstract void retryGrabOnlineData();

    @Override
    public void onPlaceHolderRetry(View view) {
        retryGrabOnlineData();
    }

    @Override
    public void onError() {
        showPlaceholder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onActionFailed() {
        DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
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

    /**
     * item上右下角的三个点按钮的操作
     * 在售列表中为 {修改物品信息} 和 {下架物品}
     * 已售和已购列表中的按钮动作待定
     */
    public abstract void onItemAction(View view, DomainGoodsInfo.DataBean bean);

    @Override
    public void onItemActionButtonClick(View view, DomainGoodsInfo.DataBean bean) {
        onItemAction(view, bean);
    }
}

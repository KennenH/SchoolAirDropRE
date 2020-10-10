package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

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
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

import java.util.List;

public abstract class SSBBaseFragment extends StatePlaceholderFragment
        implements BaseStateViewModel.OnRequestListener,
        StatePlaceHolder.OnPlaceHolderRefreshListener, SSBViewModel.OnSSBActionListener,
        SSBAdapter.OnSSBItemActionListener {

    public static final int SELLING_POS = 0;
    public static final int SOLD_POS = 1;
    public static final int BOUGHT_POS = 2;


    protected SSBViewModel viewModel;

    protected SSBAdapter mAdapter;

    protected com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding;
    protected List<DomainGoodsInfo.DataBean> mList;

    protected OnSSBDataLenChangedListener mOnSSBDataLenChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SSBViewModel.class);
        viewModel.setOnRequestListener(this);
        viewModel.setOnSSBActionListener(this);

        binding.ssbRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.placeHolder.setOnPlaceHolderActionListener(this);

        mAdapter = new SSBAdapter();
        mAdapter.setOnSSBItemActionListener(this);
        binding.ssbRecycler.addItemDecoration(new MarginItemDecoration());
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
     * 当页面数据长度改变时
     * 包括
     * 1、子fragment切换
     * 2、添加或删除数据
     * 重新统计当前子fragment的数据条数并显示
     */
    public interface OnSSBDataLenChangedListener {
        /**
         * 数据长度改变
         */
        void onSSBDataLenChanged(int total, int page);
    }

    public void setOnSSBDataLenChangedListener(OnSSBDataLenChangedListener listener) {
        mOnSSBDataLenChangedListener = listener;
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
     * validate mList's size
     */
    protected void dataLenOnChange(int page) {
        if (mOnSSBDataLenChangedListener != null && mList != null)
            mOnSSBDataLenChangedListener.onSSBDataLenChanged(mList.size(), page);
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

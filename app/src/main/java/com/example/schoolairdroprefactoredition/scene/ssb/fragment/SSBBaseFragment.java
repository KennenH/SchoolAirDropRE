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

import com.example.schoolairdroprefactoredition.application.Application;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.example.schoolairdroprefactoredition.viewmodel.SellingViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SSBBaseFragment extends StatePlaceholderFragment
        implements BaseStateViewModel.OnRequestListener,
        StatePlaceHolder.OnPlaceHolderRefreshListener,
        SSBAdapter.OnSSBItemActionListener {

    public static final int SELLING_POS = 0;

    protected SellingViewModel viewModel;

    protected SSBAdapter mAdapter;

    protected com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding;
    protected List<DomainPurchasing.DataBean> mList;

    protected OnSSBDataLenChangedListener mOnSSBDataLenChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SellingViewModel.class);

        binding.ssbRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.placeHolder.addOnPlaceHolderActionListener(this);

        mAdapter = new SSBAdapter(getActivity().getIntent().getBooleanExtra(ConstantUtil.KEY_IS_MINE, false));
        mAdapter.addOnSSBItemActionListener(this);
        binding.ssbRecycler.addItemDecoration(new MarginItemDecoration());
        binding.ssbRecycler.setAdapter(mAdapter);
        binding.ssbRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity) {
                    ((SSBActivity) getActivity()).hideSearchBar();
                }
            }
        });
        init(binding);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public StatePlaceHolder getStatePlaceholder() {
        return binding.placeHolder;
    }

    @NotNull
    @Override
    public View getContentContainer() {
        return binding.ssbRecycler;
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
    protected void loadData(DomainPurchasing data) {
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
    protected DomainToken getToken() {
        try {
            return ((Application) getActivity().getApplication()).getCachedToken();
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    protected int getUserID() {
        int userInfo = -1;
        try {
            userInfo = getActivity().getIntent().getExtras().getInt(ConstantUtil.KEY_USER_ID);
        } catch (Exception ignored) {
        }
        return userInfo;
    }

    /**
     * 当某个页面数据组改变时要通知Adapter是哪个页面的数据发生了改变
     * 便于{@link SSBActivity}即时通知{@link com.example.schoolairdroprefactoredition.ui.components.SSBFilter}
     * 重新统计页面数据总数
     */
    protected void dataLenOnChange(int page) {
        if (mOnSSBDataLenChangedListener != null && mList != null) {
            mOnSSBDataLenChangedListener.onSSBDataLenChanged(mList.size(), page);
        }
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
        dismissLoading(() -> showPlaceholder(StatePlaceHolder.TYPE_ERROR));
    }

    /**
     * item上右下角的三个点按钮的操作
     * 在售列表中为 {修改物品信息} 和 {下架物品}
     */
    protected void onItemAction(View view, DomainPurchasing.DataBean bean) {
    }

    @Override
    public void onItemActionButtonClick(View view, DomainPurchasing.DataBean bean) {
        onItemAction(view, bean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.addOnSSBItemActionListener(this);
        }
        mOnSSBDataLenChangedListener = null;
    }
}

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
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.util.List;

public abstract class SSBBaseFragment extends Fragment implements BaseStateViewModel.OnRequestListener, SSBFilter.OnFilterListener, StatePlaceHolder.OnPlaceHolderRefreshListener, SSBViewModel.OnSSBActionListener, SSBAdapter.OnSSBItemActionListener {

    protected SSBViewModel viewModel;

    protected SSBAdapter mAdapter;
    protected SSBFilter mFilter;

    protected LoadingPopupView mLoading;

    protected com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding;
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
        binding = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SSBViewModel.class);
        viewModel.setOnRequestListener(this);
        viewModel.setOnSSBActionListener(this);

        mLoading = MyUtil.loading(getContext());
        binding.ssbRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.placeHolder.setOnPlaceHolderActionListener(this);

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter(bundle);
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

    protected void showLoading() {
        if (mLoading == null) mLoading = MyUtil.loading(getContext());
        mLoading.show();
    }

    protected void dismissLoading() {
        if (mLoading != null) mLoading.dismiss();
    }

    /**
     * 子fragment在获取网络数据之后调用
     */
    protected void loadData(DomainGoodsInfo data) {
        mList = data.getData();
        if (mList.size() == 0)
            showPlaceHolder(StatePlaceHolder.TYPE_EMPTY);
        else {
            mAdapter.setList(mList);
            showContentContainer();
        }
    }

    /**
     * 显示placeholder
     *
     * @param type one of
     *             {@link StatePlaceHolder#TYPE_LOADING}
     *             {@link StatePlaceHolder#TYPE_EMPTY}
     *             {@link StatePlaceHolder#TYPE_NETWORK_OR_LOCATION_ERROR_HOME}
     *             {@link StatePlaceHolder#TYPE_ERROR}
     */
    protected void showPlaceHolder(int type) {
        binding.placeHolder.setPlaceHolderType(type);
        binding.placeHolder.setVisibility(View.VISIBLE);
        binding.ssbRecycler.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示物品列表
     */
    protected void showContentContainer() {
        binding.placeHolder.setVisibility(View.INVISIBLE);
        binding.ssbRecycler.setVisibility(View.VISIBLE);
    }

    public abstract void init(FragmentSsbBinding binding);

    public abstract void retryGrabOnlineData();

    @Override
    public void onPlaceHolderRetry(View view) {
        retryGrabOnlineData();
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onActionFailed() {
        MyUtil.showCenterDialog(getContext(), MyUtil.DIALOG_TYPE.ERROR_UNKNOWN);
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

    public abstract void onItemAction(View view, DomainGoodsInfo.DataBean bean);

    @Override
    public void onItemActionButtonClick(View view, DomainGoodsInfo.DataBean bean) {
        onItemAction(view, bean);
    }
}

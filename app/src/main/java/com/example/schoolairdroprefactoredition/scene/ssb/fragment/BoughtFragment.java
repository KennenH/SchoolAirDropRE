package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.content.Context;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

/**
 * {@link SellingFragment}
 * {@link SoldFragment}
 */
public class BoughtFragment extends SSBBaseFragment implements SSBActivity.OnLoginStateChangeListener, SSBActivity.OnChangedToBoughtListener {
    public static BoughtFragment newInstance() {
        BoughtFragment fragment = new BoughtFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof SSBActivity) {
            ((SSBActivity) getActivity()).setOnLoginStateChangeListener(this);
            ((SSBActivity) getActivity()).setOnChangedToBoughtListener(this);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(false);
        binding.placeHolder.setOnPlaceHolderActionListener(this);
        getBought();
    }

    @Override
    public void retryGrabOnlineData() {
        getBought();
    }

    /**
     * 网络请求已购数据
     */
    private void getBought() {
        if (getToken() != null) { // 未登录
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getBought(getToken().getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                loadData(data);
                dataLenOnChange(SSBBaseFragment.BOUGHT_POS);
            });
        } else // 未登录时
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
    }

    /**
     * item上动作按钮按下的回调
     */
    @Override
    public void onItemAction(View view, DomainGoodsInfo.DataBean bean) {

    }

    @Override
    public void onLoginSSB() {
        getBought();
    }

    @Override
    public void onChangedToBought() {
        dataLenOnChange(SSBBaseFragment.BOUGHT_POS);
    }
}
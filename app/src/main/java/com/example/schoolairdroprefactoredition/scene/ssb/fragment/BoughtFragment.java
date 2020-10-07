package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

/**
 * {@link SellingFragment}
 * {@link SoldFragment}
 */
public class BoughtFragment extends SSBBaseFragment implements StatePlaceHolder.OnPlaceHolderRefreshListener {

    public static BoughtFragment newInstance(Bundle bundle) {
        BoughtFragment fragment = new BoughtFragment();
        fragment.setArguments(new Bundle(bundle));
        return fragment;
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
        if (token != null) { // 未登录
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getBought(token.getAccess_token()).observe(getViewLifecycleOwner(), this::loadData);
        } else // 未登录时
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
    }

    @Override
    public void onPlaceHolderRetry(View view) {
        getBought();
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
     * item上动作按钮按下的回调
     */
    @Override
    public void onItemAction(View view, DomainGoodsInfo.DataBean bean) {

    }
}
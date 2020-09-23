package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

/**
 * {@link SellingFragment}
 * {@link BoughtFragment}
 */
public class SoldFragment extends SSBBaseFragment implements StatePlaceHolder.OnPlaceHolderRefreshListener {

    public static SoldFragment newInstance(Bundle bundle) {
        SoldFragment fragment = new SoldFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(false);
        binding.placeHolder.setOnPlaceHolderActionListener(this);
        getSold();
    }

    /**
     * 网络请求已售物品
     */
    private void getSold() {
        if (token != null) { // 已登录
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getSold(token.getAccess_token()).observe(getViewLifecycleOwner(), this::loadData);
        } else // 未登录时
            showPlaceHolder(StatePlaceHolder.TYPE_EMPTY);
    }

    @Override
    public void onRetry(View view) {
        getSold();
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
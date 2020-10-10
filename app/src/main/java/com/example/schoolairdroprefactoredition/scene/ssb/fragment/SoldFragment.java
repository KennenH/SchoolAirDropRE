package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

/**
 * {@link SellingFragment}
 * {@link BoughtFragment}
 */
public class SoldFragment extends SSBBaseFragment implements SSBActivity.OnLoginStateChangeListener, SSBActivity.OnChangedToSoldListener {

    public static SoldFragment newInstance() {
        SoldFragment fragment = new SoldFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof SSBActivity) {
            ((SSBActivity) getActivity()).setOnLoginStateChangeListener(this);
            ((SSBActivity) getActivity()).setOnChangedToSoldListener(this);
        }
    }

    @Override
    public void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(false);
        binding.placeHolder.setOnPlaceHolderActionListener(this);
        getSold();
    }

    @Override
    public void retryGrabOnlineData() {
        getSold();
    }

    /**
     * 网络请求已售物品
     */
    private void getSold() {
        if (getToken() != null) { // 已登录
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getSold(getToken().getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                loadData(data);
                dataLenOnChange(SSBBaseFragment.SOLD_POS);
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
        getSold();
    }

    @Override
    public void onChangedToSold() {
        dataLenOnChange(SSBBaseFragment.SOLD_POS);
    }
}
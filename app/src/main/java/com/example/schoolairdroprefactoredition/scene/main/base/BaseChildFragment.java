package com.example.schoolairdroprefactoredition.scene.main.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

public class BaseChildFragment extends Fragment {

    /**
     * 只在第一个子页面加载时询问权限
     * 但是这里会有一个问题，切换app主题时由于requested已置为true
     * 因此页面被recreated的时候就不会再次自动获取数据
     * 解决：需要在onDestroy中将requested重新置为false
     */
    private static boolean requested = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!requested) {
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            locate(PermissionBaseActivity.RequestType.AUTO);// 自动请求MainActivity的定位}
            requested = true;
        }
    }

    /**
     * 显示PlaceHolder
     *
     * @param type {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_NETWORK_OR_LOCATION_ERROR_HOME}
     *             {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_EMPTY_GOODS_HOME}
     */
    protected void showPlaceHolder(int type) {
        if (getParentFragment() instanceof BaseParentFragment) {
            ((BaseParentFragment) getParentFragment()).showPlaceholder(type);
        }
    }

    /**
     * 显示物品信息
     */
    protected void showContentContainer() {
        if (getParentFragment() instanceof BaseParentFragment) {
            ((BaseParentFragment) getParentFragment()).showContentContainer();
        }
    }

    /**
     * 请求{@link MainActivity#requestPermission(String, int)} (int)} ()}的定位
     */
    protected void locate(@PermissionBaseActivity.RequestType int type) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestPermission(PermissionConstants.LOCATION, type);
        }
    }

    /**
     * 请求定位但是不请求权限
     */
    protected void locateWithoutRequest() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).checkPermissionWithoutRequest(PermissionBaseActivity.Automatically.LOCATION);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requested = false;
    }
}

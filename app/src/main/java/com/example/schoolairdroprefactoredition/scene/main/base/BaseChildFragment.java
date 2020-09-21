package com.example.schoolairdroprefactoredition.scene.main.base;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

public class BaseChildFragment extends Fragment {

    /**
     * 显示PlaceHolder
     *
     * @param type {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_ERROR}
     *             {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_EMPTY}
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
        if (getActivity() instanceof PermissionBaseActivity) {
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            ((PermissionBaseActivity) getActivity()).requestPermission(PermissionConstants.LOCATION, type);
        }
    }

    /**
     * 请求定位但是不请求权限
     */
    protected void locateWithoutRequest() {
        if (getActivity() instanceof PermissionBaseActivity) {
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            ((PermissionBaseActivity) getActivity()).checkPermissionWithoutRequest(PermissionBaseActivity.Automatically.LOCATION);
        }
    }

}

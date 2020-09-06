package com.example.schoolairdroprefactoredition.scene.main.base;

import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
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
     * 请求{@link MainActivity#requestLocationPermission(int)} ()}的定位
     */
    protected void locate(@PermissionBaseActivity.RequestType int type) {
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).requestLocationPermission(type);
    }

}

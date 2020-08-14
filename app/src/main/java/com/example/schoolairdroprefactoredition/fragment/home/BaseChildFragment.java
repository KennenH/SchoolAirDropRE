package com.example.schoolairdroprefactoredition.fragment.home;

import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.activity.MainActivity;
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
     * 请求{@link MainActivity#startLocation()}的定位
     */
    protected void locate() {
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).startLocation();
    }

}

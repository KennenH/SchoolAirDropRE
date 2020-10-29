package com.example.schoolairdroprefactoredition.scene.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * 页面中有{@link StatePlaceHolder}的通用fragment
 */
public abstract class StatePlaceholderFragment extends Fragment {
    protected LoadingPopupView mLoading;

    protected View mStatePlaceholderFragmentContainer;
    protected StatePlaceHolder mStatePlaceholderFragmentPlaceholder;

    private int retry = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoading = MyUtil.loading(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 设置页面中的主要试图和statePlaceholder的引用
     * 以便继承的fragment调用 {@link #showPlaceholder} 和{@link #showContentContainer()}
     */
    public abstract void setContainerAndPlaceholder();

    /**
     * 显示主视图
     */
    public void showContentContainer() {
        if (mStatePlaceholderFragmentContainer == null || mStatePlaceholderFragmentPlaceholder == null)
            setContainerAndPlaceholder();

        mStatePlaceholderFragmentPlaceholder.setVisibility(View.GONE);
        mStatePlaceholderFragmentContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示placeholder
     *
     * @param type 见{@link StatePlaceHolder}中的类型常量
     */
    public void showPlaceholder(int type) {
        if (mStatePlaceholderFragmentContainer == null || mStatePlaceholderFragmentPlaceholder == null)
            setContainerAndPlaceholder();

        mStatePlaceholderFragmentPlaceholder.setPlaceHolderType(type);
        mStatePlaceholderFragmentContainer.setVisibility(View.GONE);
        mStatePlaceholderFragmentPlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * 在最上层显示一个小弹窗以提示正在加载
     */
    protected void showLoading() {
        if (mLoading == null) mLoading = MyUtil.loading(getContext());
        mLoading.show();
    }

    protected void dismissLoading(Runnable task) {
        if (mLoading != null)
            mLoading.dismissWith(task);
        else task.run();
    }
}

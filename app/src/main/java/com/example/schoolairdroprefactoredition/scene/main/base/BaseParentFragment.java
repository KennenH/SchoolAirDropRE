package com.example.schoolairdroprefactoredition.scene.main.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

public class BaseParentFragment extends Fragment implements StatePlaceHolder.OnPlaceHolderActionListener{

    private StatePlaceHolder mPlaceHolder;
    private ViewPager mContentContainer;

    protected OnSearchBarClickedListener mOnSearchBarClickedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setUpPlaceHolderHAndGoodsContainer(StatePlaceHolder placeHolder, ViewPager goodsContainer) {
        mPlaceHolder = placeHolder;
        mContentContainer = goodsContainer;

        mPlaceHolder.setOnPlaceHolderActionListener(this);
    }

    /**
     * 显示placeholder 隐藏recycler list
     *
     * @param type {@link StatePlaceHolder#TYPE_ERROR}
     *             {@link StatePlaceHolder#TYPE_EMPTY}
     */
    public void showPlaceholder(int type) {
        if (mPlaceHolder == null || mContentContainer == null)
            throw new NullPointerException("mPlaceHolder or mGoodsContainer can not be null");

        mPlaceHolder.setPlaceHolderType(type);
        mPlaceHolder.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.GONE);
    }

    /**
     * 显示recycler list 隐藏placeholder
     */
    public void showContentContainer() {
        if (mPlaceHolder == null || mContentContainer == null)
            throw new NullPointerException("mPlaceHolder or mGoodsContainer can not be null");

        mPlaceHolder.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.VISIBLE);
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderActionListener}
     * 重试
     */
    @Override
    public void onRetry(View view) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestPermission(PermissionConstants.LOCATION, PermissionBaseActivity.RequestType.MANUAL);
        }
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderActionListener}
     * 发布我的物品
     */
    @Override
    public void onPostMyItem(View view) {
        if (getActivity() instanceof MainActivity)
            SellingAddNewActivity.start(getContext(), ((MainActivity) getActivity()).getBundle());
    }

    /**
     * 搜索框点击时的回调
     */
    public interface OnSearchBarClickedListener {
        void onSearchBarClicked();
    }

    public void setOnSearchBarClickedListener(OnSearchBarClickedListener listener) {
        mOnSearchBarClickedListener = listener;
    }
}

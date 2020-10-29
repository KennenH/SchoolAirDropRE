package com.example.schoolairdroprefactoredition.scene.main.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;

public class BaseParentFragment extends StatePlaceholderFragment implements StatePlaceHolder.OnPlaceHolderRefreshListener, StatePlaceHolder.OnHomePlaceHolderActionListener {

    private StatePlaceHolder mPlaceHolder;
    private ViewPager mContentContainer;

    protected OnSearchBarClickedListener mOnSearchBarClickedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 绑定页面placeholder 与 内容视图
     */
    protected void setUpPlaceHolderHAndContainerView(StatePlaceHolder placeHolder, ViewPager goodsContainer) {
        mPlaceHolder = placeHolder;
        mContentContainer = goodsContainer;

        mPlaceHolder.setOnPlaceHolderActionListener(this);
        mPlaceHolder.setOnHomePostItemListener(this);
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderRefreshListener}
     * 重试
     */
    @Override
    public void onPlaceHolderRetry(View view) {
        if (getActivity() instanceof MainActivity) {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            ((MainActivity) getActivity()).requestPermission(PermissionConstants.LOCATION, PermissionBaseActivity.RequestType.MANUAL);
        } else
            DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderRefreshListener}
     * 发布我的物品
     */
    @Override
    public void onHomePostMyItem(View view) {
        if (getActivity() instanceof MainActivity) {
            SellingAddNewActivity.start(getContext(), getActivity().getIntent().getExtras());
        } else
            DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
    }

    @Override
    public void onHomePostMyTopic(View view) {

    }

    @Override
    public void setContainerAndPlaceholder() {
        mStatePlaceholderFragmentPlaceholder = mPlaceHolder;
        mStatePlaceholderFragmentContainer = mContentContainer;
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

package com.example.schoolairdroprefactoredition.scene.main.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity;
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

        mPlaceHolder.addOnPlaceHolderActionListener(this);
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
        } else {
            DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
        }
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderRefreshListener}
     * 发布我的物品
     */
    @Override
    public void onHomePostMyItem(View view) {
        if (getActivity() instanceof MainActivity) {
            AddNewActivity.Companion.start(getContext(),AddNewActivity.AddNewType.ADD_ITEM);
        }
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderRefreshListener}
     * 发表帖子
     */
    @Override
    public void onHomePostMyPosts(View view) {
        if (getActivity() instanceof MainActivity) {
            AddNewActivity.Companion.start(getContext(), AddNewActivity.AddNewType.ADD_INQUIRY);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public StatePlaceHolder getStatePlaceholder() {
        return mPlaceHolder;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public View getContentContainer() {
        return mContentContainer;
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

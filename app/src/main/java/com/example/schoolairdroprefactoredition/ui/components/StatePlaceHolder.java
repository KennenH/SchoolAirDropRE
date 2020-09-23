package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.PlaceholderBinding;
import com.example.schoolairdroprefactoredition.ui.auto.ConstrainLayoutAuto;
import com.github.ybq.android.spinkit.SpinKitView;

/**
 * 状态显示PlaceHolder
 */
public class StatePlaceHolder extends ConstrainLayoutAuto implements View.OnClickListener {
    public static final int TYPE_LOADING = 77;//状态 类型 正在加载
    public static final int TYPE_ERROR = 88;//状态 类型 错误
    public static final int TYPE_EMPTY_HOME = 99;//状态 类型 没有物品 仅首页使用
    public static final int TYPE_EMPTY = 110;//状态 类型 没有物品
    public static final int TYPE_DENIED = 11;//状态 类型 权限被拒
    public static final int TYPE_UNKNOWN = 22;//状态 类型 未知错误
    private static final int TYPE_NULL = 33;

    private static final int ICON_ERROR = R.drawable.ic_location_error;// 状态 图标 错误
    private static final int ICON_EMPTY = R.drawable.ic_empty;// 状态 图标 空
    private static final int ICON_DENIED = R.drawable.ic_reject;// 状态 图标 权限被拒
    private static final int ICON_UNKNOWN = R.drawable.ic_error_unknown;// 状态 图标 未知错误

    private static final int TIP_ERROR = R.string.errorNetLocation;// 状态 提示 错误
    private static final int TIP_EMPTY_HOME = R.string.errorEmpty;// 状态 提示 空 仅首页使用
    private static final int TIP_EMPTY = R.string.nothingThere;// 状态 提示 空 非首页
    private static final int TIP_DENIED = R.string.permissionDenied;// 状态 提示 权限被拒
    private static final int TIP_UNKNOWN = R.string.errorUnknown;// 状态 提示 未知错误

    private ImageView mIcon;
    private TextView mTip;
    private ImageView mRefresh;
    private TextView mAction;
    private SpinKitView mLoading;

    private int type = TYPE_UNKNOWN;

    private OnPlaceHolderRefreshListener mOnPlaceHolderRefreshListener;
    private OnHomePostItemListener mOnHomePostItemListener;

    public StatePlaceHolder(Context context) {
        this(context, null);
    }

    public StatePlaceHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatePlaceHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlaceholderBinding binding = PlaceholderBinding.bind(LayoutInflater.from(context).inflate(R.layout.placeholder, this, true));

        mIcon = binding.imageView;
        mTip = binding.textView;
        mRefresh = binding.refresh;
        mAction = binding.button;
        mLoading = binding.loading;

        mRefresh.setOnClickListener(this);
        mAction.setOnClickListener(this);
    }

    /**
     * 设置placeholder类型
     * one of below
     *
     * @param type {@link StatePlaceHolder#TYPE_EMPTY_HOME}
     *             {@link StatePlaceHolder#TYPE_EMPTY}
     *             {@link StatePlaceHolder#TYPE_LOADING}
     *             {@link StatePlaceHolder#TYPE_ERROR}
     *             {@link StatePlaceHolder#TYPE_DENIED}
     *             {@link StatePlaceHolder#TYPE_UNKNOWN}
     *             {@link StatePlaceHolder#TYPE_NULL}
     */
    public void setPlaceHolderType(int type) {
        this.type = type;
        if (type == TYPE_ERROR) {
            // 网络或定位出错
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_ERROR);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_ERROR);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(GONE);
        } else if (type == TYPE_EMPTY_HOME) {
            // 附近没有物品 仅首页使用
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY_HOME);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(VISIBLE);
            mAction.setText(R.string.postMyItem);
        } else if (type == TYPE_DENIED) {
            // 权限拒绝
            mLoading.setVisibility(GONE);
            mRefresh.setVisibility(GONE);
            mIcon.setImageResource(ICON_DENIED);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_DENIED);
            mTip.setVisibility(VISIBLE);
            mAction.setText(R.string.errorRetry);
            mAction.setVisibility(VISIBLE);
        } else if (type == TYPE_LOADING) {
            // 正在加载
            mIcon.setVisibility(GONE);
            mTip.setVisibility(GONE);
            mRefresh.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mLoading.setVisibility(VISIBLE);
        } else if (type == TYPE_EMPTY) {
            // 没有物品 非首页
            mLoading.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mIcon.setImageResource(ICON_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
        } else {
            // 未知错误
            mLoading.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mIcon.setImageResource(ICON_UNKNOWN);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_UNKNOWN);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
        }
    }

    /**
     * 状态placeholder 重试监听器
     */
    public interface OnPlaceHolderRefreshListener {
        /**
         * 重试
         */
        void onRetry(View view);
    }

    /**
     * 设置重试监听器
     */
    public void setOnPlaceHolderActionListener(OnPlaceHolderRefreshListener listener) {
        mOnPlaceHolderRefreshListener = listener;
    }

    /**
     * 首页发布我的物品监听器
     */
    public interface OnHomePostItemListener {
        /**
         * 发布我的物品 打开{@link com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity}
         */
        void onHomePostMyItem(View view);
    }

    public void setOnHomePostItemListener(OnHomePostItemListener listener) {
        mOnHomePostItemListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.refresh) {
            if (mOnPlaceHolderRefreshListener != null) {
                mOnPlaceHolderRefreshListener.onRetry(v);
            }
        } else if (id == R.id.button) {
            if (type == TYPE_EMPTY_HOME) {
                if (mOnPlaceHolderRefreshListener != null)
                    mOnHomePostItemListener.onHomePostMyItem(v); // 当附近无物品时按钮效果为发布物品
            } else if (type == TYPE_DENIED) {
                if (mOnHomePostItemListener != null)
                    mOnPlaceHolderRefreshListener.onRetry(v); // 否则当权限被拒绝时为开启权限
            }

        }
    }
}

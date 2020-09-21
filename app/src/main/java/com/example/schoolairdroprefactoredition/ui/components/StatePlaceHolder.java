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
    public static final int TYPE_EMPTY = 99;//状态 类型 没有物品
    public static final int TYPE_DENIED = 11;//状态 类型 权限被拒
    public static final int TYPE_UNKNOWN = 22;//状态 类型 未知错误

    private static final int ICON_ERROR = R.drawable.ic_location_error;// 状态 图标 错误
    private static final int ICON_EMPTY = R.drawable.ic_empty;// 状态 图标 空
    private static final int ICON_DENIED = R.drawable.ic_reject;// 状态 图标 权限被拒
    private static final int ICON_UNKNOWN = R.drawable.ic_error_unknown;// 状态 图标 未知错误

    private static final int TIP_ERROR = R.string.errorNetLocation;// 状态 提示 错误
    private static final int TIP_EMPTY = R.string.errorEmpty;// 状态 提示 空
    private static final int TIP_DENIED = R.string.permissionDenied;// 状态 提示 权限被拒
    private static final int TIP_UNKNOWN = R.string.errorUnknown;// 状态 提示 未知错误

    private ImageView mIcon;
    private TextView mTip;
    private ImageView mRefresh;
    private TextView mAction;
    private SpinKitView mLoading;

    private int type = TYPE_LOADING;

    private OnPlaceHolderActionListener mOnPlaceHolderActionListener;

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

        setPlaceHolderType(TYPE_LOADING);
    }

    /**
     * 设置placeholder类型
     *
     * @param type {@link StatePlaceHolder#TYPE_EMPTY}
     *             {@link StatePlaceHolder#TYPE_LOADING}
     *             {@link StatePlaceHolder#TYPE_ERROR}
     *             {@link StatePlaceHolder#TYPE_DENIED}
     *             {@link StatePlaceHolder#TYPE_UNKNOWN}
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
            mAction.setVisibility(INVISIBLE);
        } else if (type == TYPE_EMPTY) {
            // 附近没有物品
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(VISIBLE);
            mAction.setText(R.string.postMyItem);
        } else if (type == TYPE_DENIED) {
            // 权限拒绝
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_DENIED);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_DENIED);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(GONE);
            mAction.setText(R.string.errorRetry);
            mAction.setVisibility(VISIBLE);
        } else if (type == TYPE_LOADING) {
            // 正在加载
            mLoading.setVisibility(VISIBLE);
            mIcon.setVisibility(GONE);
            mTip.setVisibility(GONE);
            mRefresh.setVisibility(GONE);
            mAction.setVisibility(GONE);
        } else {
            // 未知错误
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_UNKNOWN);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_UNKNOWN);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(INVISIBLE);
        }
    }

    /**
     * 状态动作监听器
     */
    public interface OnPlaceHolderActionListener {
        /**
         * 重试 重新请求定位
         *
         * @param view
         */
        void onRetry(View view);

        /**
         * 发布我的物品 打开{@link com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity}
         *
         * @param view
         */
        void onPostMyItem(View view);
    }

    /**
     * 设置状态动作监听器
     */
    public void setOnPlaceHolderActionListener(OnPlaceHolderActionListener listener) {
        mOnPlaceHolderActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.refresh) {
            if (mOnPlaceHolderActionListener != null) {
                mOnPlaceHolderActionListener.onRetry(v);
            }
        } else if (id == R.id.button) {
            if (mOnPlaceHolderActionListener != null) {
                if (type == TYPE_EMPTY)
                    mOnPlaceHolderActionListener.onPostMyItem(v); // 当附近无物品时按钮效果为发布物品
                else
                    mOnPlaceHolderActionListener.onRetry(v); // 否则当权限被拒绝时为开启权限
            }
        }
    }
}

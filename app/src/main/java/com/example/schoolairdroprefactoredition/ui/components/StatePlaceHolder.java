package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.auto.ConstrainLayoutAuto;

/**
 * 状态显示PlaceHolder
 */
public class StatePlaceHolder extends ConstrainLayoutAuto implements View.OnClickListener {
    public static final int TYPE_LOADING = 77;//状态 类型 正在加载
    public static final int TYPE_ERROR = 88;//状态 类型 错误
    public static final int TYPE_EMPTY = 99;//状态 类型 空

    private static final int ICON_LOADING = R.drawable.ic_loading;// 状态 图标 正在加载
    private static final int ICON_ERROR = R.drawable.ic_error;// 状态 图标 错误
    private static final int ICON_EMPTY = R.drawable.ic_empty;// 状态 图标 空

    private static final int TIP_ERROR = R.string.errorNetLocation;// 状态 提示 错误
    private static final int TIP_EMPTY = R.string.errorEmpty;// 状态 提示 空

    private Animation mLoadingAnim;

    private ImageView mIcon;
    private TextView mTip;
    private ImageView mRefresh;
    private TextView mAction;

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
        LayoutInflater.from(context).inflate(R.layout.placeholder, this, true);

        mIcon = findViewById(R.id.imageView);
        mTip = findViewById(R.id.textView);
        mRefresh = findViewById(R.id.refresh);
        mAction = findViewById(R.id.button);
        mAction.setOnClickListener(this);

        mLoadingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotation_infinite);

        setPlaceHolderType(TYPE_LOADING);
    }

    /**
     * 设置placeholder类型
     *
     * @param type {@link StatePlaceHolder#TYPE_EMPTY}
     *             {@link StatePlaceHolder#TYPE_LOADING}
     *             {@link StatePlaceHolder#TYPE_ERROR}
     */
    public void setPlaceHolderType(int type) {
        if (type != TYPE_EMPTY && type != TYPE_ERROR && type != TYPE_LOADING)
            throw new IllegalArgumentException("no such type of placeholder");

        this.type = type;
        if (type == TYPE_ERROR) {
            // 网络或定位出错
            mIcon.setImageResource(ICON_ERROR);
            mTip.setText(TIP_ERROR);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(INVISIBLE);
        } else if (type == TYPE_EMPTY) {
            // 附近没有物品
            mIcon.setImageResource(ICON_EMPTY);
            mTip.setText(TIP_EMPTY);
            mRefresh.setVisibility(VISIBLE);
            mTip.setVisibility(VISIBLE);
            mAction.setVisibility(VISIBLE);
        } else {
            // 正在加载
            mIcon.setImageResource(ICON_LOADING);
            mTip.setVisibility(GONE);
            mRefresh.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mIcon.startAnimation(mLoadingAnim);
        }
    }

    /**
     * 当被设置为不可见时取消加载动画
     *
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE)
            mLoadingAnim.cancel();
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
         * 发布我的物品 打开{@link com.example.schoolairdroprefactoredition.activity.addnew.SellingAddNewActivity}
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
                mOnPlaceHolderActionListener.onPostMyItem(v);
            }
        }
    }
}

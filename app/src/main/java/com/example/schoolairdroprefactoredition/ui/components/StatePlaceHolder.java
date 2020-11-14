package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.PlaceholderBinding;
import com.example.schoolairdroprefactoredition.domain.base.LoadState;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;
import com.github.ybq.android.spinkit.SpinKitView;

/**
 * 状态显示PlaceHolder
 */
public class StatePlaceHolder extends ConstraintLayoutAuto implements View.OnClickListener {

    public static final int TYPE_EMPTY_GOODS_HOME = 99;//状态 类型 没有物品 仅首页使用
    public static final int TYPE_EMPTY_NEWS_HOME = 66;//状态 类型 没有附近话题 仅首页使用
    public static final int TYPE_EMPTY = 110;//状态 类型 没有物品
    public static final int TYPE_EMPTY_SEARCH = 220;//状态 类型 搜索为空
    public static final int TYPE_LOADING = 77;//状态 类型 正在加载
    public static final int TYPE_NETWORK_OR_LOCATION_ERROR_HOME = 88;//状态 类型 网络或定位错误 仅首页使用
    public static final int TYPE_DENIED = 11;//状态 类型 权限被拒
    public static final int TYPE_ERROR = 22;//状态 类型 加载错误

    private static final int ICON_NETWORK_OR_LOCATION_ERROR_HOME = R.drawable.ic_location_error;// 状态 图标 网络或定位错误 仅首页使用
    private static final int ICON_GOODS_EMPTY = R.drawable.ic_empty;// 状态 图标 物品空
    private static final int ICON_NEWS_EMPTY = R.drawable.ic_empty_news;// 状态 图标 新闻空
    private static final int ICON_DENIED = R.drawable.ic_reject;// 状态 图标 权限被拒
    private static final int ICON_ERROR = R.drawable.ic_error_unknown;// 状态 图标 加载错误

    private static final int TIP_NETWORK_OR_LOCATION_ERROR_HOME = R.string.errorNetLocation;// 状态 提示 网络或定位错误 仅首页使用
    private static final int TIP_EMPTY_GOODS_HOME = R.string.errorGoodsEmptyHome;// 状态 提示 物品空 仅首页使用
    private static final int TIP_EMPTY_NEWS_HOME = R.string.errorNewsEmptyHome;// 状态 提示 新闻空 仅首页使用
    private static final int TIP_EMPTY = R.string.nothingThere;// 状态 提示 空 非首页
    private static final int TIP_EMPTY_SEARCH = R.string.emptySearch;// 状态 提示 空 搜索
    private static final int TIP_DENIED = R.string.permissionDenied;// 状态 提示 权限被拒
    private static final int TIP_ERROR = R.string.errorLoading;// 状态 提示 加载错误

    private final ImageView mIcon;
    private final TextView mTip;
    private final ImageView mRefresh;
    private final TextView mAction;
    private final SpinKitView mLoading;

    private int type = TYPE_ERROR;

    private OnPlaceHolderRefreshListener mOnPlaceHolderRefreshListener;
    private OnHomePlaceHolderActionListener mOnHomePlaceHolderActionListener;

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

        setPlaceholderType(TYPE_LOADING);
    }

    /**
     * 设置placeholder类型
     * one of below
     *
     * @param type {@link StatePlaceHolder#TYPE_EMPTY_GOODS_HOME}
     *             {@link StatePlaceHolder#TYPE_EMPTY_NEWS_HOME}
     *             {@link StatePlaceHolder#TYPE_EMPTY}
     *             {@link StatePlaceHolder#TYPE_EMPTY_SEARCH}
     *             {@link StatePlaceHolder#TYPE_LOADING}
     *             {@link StatePlaceHolder#TYPE_NETWORK_OR_LOCATION_ERROR_HOME}
     *             {@link StatePlaceHolder#TYPE_DENIED}
     *             {@link StatePlaceHolder#TYPE_ERROR}
     * @deprecated 改用
     *              {@link StatePlaceHolder#setPlaceholderType(LoadState)}
     *
     */
    public void setPlaceholderType(int type) {
        this.type = type;
        if (type == TYPE_NETWORK_OR_LOCATION_ERROR_HOME) {
            // 网络或定位出错
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_NETWORK_OR_LOCATION_ERROR_HOME);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_NETWORK_OR_LOCATION_ERROR_HOME);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(GONE);
        } else if (type == TYPE_EMPTY_GOODS_HOME) {
            // 附近没有物品 仅首页使用
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_GOODS_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY_GOODS_HOME);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(VISIBLE);
            mAction.setText(R.string.postMyItem);
        } else if (type == TYPE_EMPTY_NEWS_HOME) {
            // 附近没有物品 仅首页使用
            mLoading.setVisibility(GONE);
            mIcon.setImageResource(ICON_NEWS_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY_NEWS_HOME);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
            mAction.setVisibility(VISIBLE);
            mAction.setText(R.string.postMyTopic);
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
            mIcon.setImageResource(ICON_GOODS_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
        } else if (type == TYPE_EMPTY_SEARCH) {
            // 没有物品 搜索
            mLoading.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mIcon.setImageResource(ICON_GOODS_EMPTY);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_EMPTY_SEARCH);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
        } else {
            // 未知错误
            mLoading.setVisibility(GONE);
            mAction.setVisibility(GONE);
            mIcon.setImageResource(ICON_ERROR);
            mIcon.setVisibility(VISIBLE);
            mTip.setText(TIP_ERROR);
            mTip.setVisibility(VISIBLE);
            mRefresh.setVisibility(VISIBLE);
        }
    }

    public void setPlaceholderType(LoadState type) {
        
    }

    /**
     * 状态placeholder 重试监听器
     */
    public interface OnPlaceHolderRefreshListener {
        /**
         * 重试
         */
        void onPlaceHolderRetry(View view);
    }

    /**
     * 设置重试监听器
     */
    public void setOnPlaceHolderActionListener(OnPlaceHolderRefreshListener listener) {
        mOnPlaceHolderRefreshListener = listener;
    }

    /**
     * 首页按钮监听器
     */
    public interface OnHomePlaceHolderActionListener {
        /**
         * 发布我的物品 打开{@link com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity}
         */
        void onHomePostMyItem(View view);

        /**
         * 发布我的话题
         */
        void onHomePostMyTopic(View view);
    }

    public void setOnHomePostItemListener(OnHomePlaceHolderActionListener listener) {
        mOnHomePlaceHolderActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.refresh) {
            if (mOnPlaceHolderRefreshListener != null) {
                mOnPlaceHolderRefreshListener.onPlaceHolderRetry(v);
            }
        } else if (id == R.id.button) {
            if (type == TYPE_EMPTY_GOODS_HOME) {
                if (mOnPlaceHolderRefreshListener != null)
                    mOnHomePlaceHolderActionListener.onHomePostMyItem(v); // 当附近无物品时按钮效果为发布物品
            } else if (type == TYPE_DENIED) {
                if (mOnHomePlaceHolderActionListener != null)
                    mOnPlaceHolderRefreshListener.onPlaceHolderRetry(v); // 否则当权限被拒绝时为开启权限
            } else if (type == TYPE_EMPTY_NEWS_HOME) {
                if (mOnHomePlaceHolderActionListener != null)
                    mOnHomePlaceHolderActionListener.onHomePostMyTopic(v);
            }

        }
    }
}

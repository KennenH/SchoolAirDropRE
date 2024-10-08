package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.PlaceholderBinding
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.github.ybq.android.spinkit.SpinKitView

/**
 * 状态显示PlaceHolder
 */
class StatePlaceHolder @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayoutAuto(context, attrs, defStyleAttr) {

    companion object {
        const val TYPE_EMPTY_HOME_GOODS = 99 //状态 类型 没有物品 仅首页使用
        const val TYPE_EMPTY_IWANT = 66 //状态 类型 没有求购 仅首页使用
        const val TYPE_EMPTY = 110 //状态 类型 没有物品
        const val TYPE_EMPTY_SEARCH = 220 //状态 类型 搜索为空
        const val TYPE_LOADING = 77 //状态 类型 正在加载
        const val TYPE_NETWORK_OR_LOCATION_ERROR_HOME = 886 //状态 类型 网络或定位错误 仅首页使用
        const val TYPE_DENIED = 112 //状态 类型 权限被拒
        const val TYPE_ERROR = 223 //状态 类型 加载错误

        private const val TIP_NETWORK_OR_LOCATION_ERROR_HOME = R.string.errorNetLocation // 状态 提示 网络或定位错误 仅首页使用
        private const val TIP_EMPTY_GOODS = R.string.errorGoodsEmptyHome // 状态 提示 物品空 仅首页使用
        private const val TIP_EMPTY_IWANT = R.string.errorIWantEmpty // 状态 提示 求购空 仅首页使用
        private const val TIP_EMPTY = R.string.nothingThere // 状态 提示 空 非首页
        private const val TIP_EMPTY_SEARCH = R.string.emptySearch // 状态 提示 空 搜索
        private const val TIP_PERMISSION_DENIED = R.string.permissionDenied // 状态 提示 权限被拒
        private const val TIP_ERROR = R.string.errorLoading // 状态 提示 加载错误
    }

    private val mIcon: ImageView
    private val mTip: TextView
    private val mLoading: SpinKitView
    private val mActionTip: TextView

    init {
        val binding = PlaceholderBinding.bind(LayoutInflater.from(context).inflate(R.layout.placeholder, this, true))
        binding.placeholderRoot.setOnClickListener {
            if (isEnableTooFrequentCheck && type != TYPE_LOADING) {
                JsonCacheUtil.runWithFrequentCheck(context, {
                    placeHolderActionListener?.onRetry(it)
                }) {
                    DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.actionTooFrequent)
                }
            }
        }
        mIcon = binding.imageView
        mTip = binding.textView
        mLoading = binding.loading
        mActionTip = binding.placeholderTip
        setPlaceholderType(TYPE_LOADING)
    }

    /**
     * 当前实例的类型，初始化为加载
     */
    private var type = TYPE_LOADING

    /**
     * 是否开启非加载状态下的点击频繁检查
     *
     * true的时候点击placeholder多次将会导致进入冷却事件而无法再次点击
     */
    private var isEnableTooFrequentCheck = false

    private var placeHolderActionListener: OnStatePlaceholderActionListener? = null

    /**
     * 设置placeholder类型
     * one of below
     *
     * @param type [StatePlaceHolder.TYPE_EMPTY_HOME_GOODS]
     * [StatePlaceHolder.TYPE_EMPTY_IWANT]
     * [StatePlaceHolder.TYPE_EMPTY]
     * [StatePlaceHolder.TYPE_EMPTY_SEARCH]
     * [StatePlaceHolder.TYPE_LOADING]
     * [StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME]
     * [StatePlaceHolder.TYPE_DENIED]
     * [StatePlaceHolder.TYPE_ERROR]
     */
    fun setPlaceholderType(type: Int) {
        this.type = type
        mIcon.visibility = VISIBLE
        mTip.visibility = VISIBLE
        mActionTip.visibility = VISIBLE
        mLoading.visibility = GONE
        when (type) {
            // 网络或定位出错
            TYPE_NETWORK_OR_LOCATION_ERROR_HOME -> {
                mTip.setText(TIP_NETWORK_OR_LOCATION_ERROR_HOME)
            }
            // 附近没有物品 仅首页使用
            TYPE_EMPTY_HOME_GOODS -> {
                mTip.setText(TIP_EMPTY_GOODS)
            }
            // 附近没有求购 仅首页使用
            TYPE_EMPTY_IWANT -> {
                mTip.setText(TIP_EMPTY_IWANT)
            }
            // 权限拒绝
            TYPE_DENIED -> {
                mTip.setText(TIP_PERMISSION_DENIED)
            }
            // 正在加载
            TYPE_LOADING -> {
                mIcon.visibility = GONE
                mTip.visibility = GONE
                mActionTip.visibility = GONE
                mLoading.visibility = VISIBLE
            }
            // 没有物品 非首页
            TYPE_EMPTY -> {
                mTip.setText(TIP_EMPTY)
            }
            // 没有结果 搜索
            TYPE_EMPTY_SEARCH -> {
                mTip.setText(TIP_EMPTY_SEARCH)
            }
            // 未知错误
            else -> {
                mTip.setText(TIP_ERROR)
            }
        }
    }

    /**
     * 设置占位图的小字提示部分
     */
    fun setPlaceholderActionTip(tip: String?) {
        if (tip != null && type != TYPE_LOADING) {
            mActionTip.visibility = View.VISIBLE
            mActionTip.text = tip
        } else {
            mActionTip.visibility = View.GONE
        }
    }

    /**
     * state placeholder 动作监听器
     */
    interface OnStatePlaceholderActionListener {
        /**
         * 轻点以重试
         */
        fun onRetry(view: View)
    }

    /**
     * 设置了placeholder动作监听之后将开启tooFrequentCheck
     */
    fun setOnStatePlaceholderActionListener(listener: OnStatePlaceholderActionListener?) {
        isEnableTooFrequentCheck = true
        placeHolderActionListener = listener
    }
}
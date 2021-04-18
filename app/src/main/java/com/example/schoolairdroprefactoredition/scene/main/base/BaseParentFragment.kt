package com.example.schoolairdroprefactoredition.scene.main.base

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.constant.PermissionConstants
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder.OnStatePlaceholderActionListener

/**
 * 主页面淘物和求购父页面的父类
 * [com.example.schoolairdroprefactoredition.scene.main.home.PurchasingParentFragment]
 * [com.example.schoolairdroprefactoredition.scene.main.home.IDesireParentFragment]
 */
open class BaseParentFragment : StatePlaceholderFragment(), OnStatePlaceholderActionListener {

    private var mPlaceHolder: StatePlaceHolder? = null

    private var mContentContainer: ViewPager? = null

    protected var mOnSearchBarClickedListener: OnSearchBarClickedListener? = null

    /**
     * 绑定页面placeholder 与 内容视图
     */
    protected fun setUpPlaceHolderHAndContainerView(placeHolder: StatePlaceHolder?, goodsContainer: ViewPager?) {
        mPlaceHolder = placeHolder
        mContentContainer = goodsContainer
        mPlaceHolder?.setOnStatePlaceholderActionListener(this)
    }

    /**
     * placeholder引导的重试回调
     */
    override fun onRetry(view: View) {
        if (activity is MainActivity) {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING)
            (activity as? MainActivity)?.requestPermission(PermissionConstants.LOCATION, PermissionBaseActivity.RequestType.MANUAL)
        }
    }

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return mPlaceHolder
    }

    override fun getContentContainer(): View? {
        return mContentContainer
    }

    /**
     * 搜索框点击时的回调
     */
    interface OnSearchBarClickedListener {
        fun onSearchBarClicked()
    }

    fun setOnSearchBarClickedListener(listener: OnSearchBarClickedListener) {
        mOnSearchBarClickedListener = listener
    }
}
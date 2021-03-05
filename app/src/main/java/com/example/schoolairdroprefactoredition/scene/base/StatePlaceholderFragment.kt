package com.example.schoolairdroprefactoredition.scene.base

import android.view.View
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * layout中有placeholder的页面可以以此fragment为父类，
 */
abstract class StatePlaceholderFragment : BaseFragment() {

    protected val mLoading: LoadingPopupView by lazy {
        MyUtil.loading(context)
    }

    /**
     * 获取页面中的placeholder
     */
    abstract fun getStatePlaceholder(): StatePlaceHolder?

    /**
     * 页面中的数据容器
     */
    abstract fun getContentContainer(): View?

    /**
     * 显示占位视图
     */
    fun showPlaceholder(type: Int, tip: String? = null) {
        getStatePlaceholder()?.setPlaceholderType(type)
        getStatePlaceholder()?.visibility = View.VISIBLE
        getContentContainer()?.visibility = View.GONE
        getStatePlaceholder()?.setPlaceholderActionTip(tip)
    }

    /**
     * 显示
     */
    fun showContentContainer() {
        getStatePlaceholder()?.visibility = View.GONE
        getContentContainer()?.visibility = View.VISIBLE
    }

    /**
     * 在最上层显示一个小弹窗以提示正在加载
     */
    protected open fun showLoading() {
        mLoading.show()
    }

    protected open fun dismissLoading(task: Runnable) {
        mLoading.dismissWith(task)
    }
}
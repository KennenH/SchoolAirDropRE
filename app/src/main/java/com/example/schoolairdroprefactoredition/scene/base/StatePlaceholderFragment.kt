package com.example.schoolairdroprefactoredition.scene.base

import android.view.View
import androidx.fragment.app.Fragment
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.lxj.xpopup.impl.LoadingPopupView

abstract class StatePlaceholderFragment : Fragment() {

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

    fun showPlaceholder(type: Int) {
        getStatePlaceholder()?.setPlaceholderType(type)
        getStatePlaceholder()?.visibility = View.VISIBLE
        getContentContainer()?.visibility = View.GONE
    }

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
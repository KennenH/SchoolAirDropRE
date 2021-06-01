package com.example.schoolairdroprefactoredition.scene.base

import android.view.View
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * layout中有placeholder的页面可以以此fragment为父类，
 */
abstract class StatePlaceholderFragment : BaseFragment() {

    protected val mLoading: LoadingPopupView? by lazy {
        context?.let { MyUtil.loading(it) }
    }

    /**
     * 指定页面中的占位视图，类型一定要是[StatePlaceHolder]
     */
    abstract fun getStatePlaceholder(): StatePlaceHolder?

    /**
     * 指定页面中的内容容器，只要是view就可以
     */
    abstract fun getContentContainer(): View?

    /**
     * 显示占位视图
     *
     * @param type 占位视图类型，具体查看[StatePlaceHolder]
     * @param tip 小字提示部分，一般用来提示用户该如何操作才能避免再次看到占位符，有的话还能稍微好看一点
     */
    fun showPlaceholder(type: Int, tip: String? = null) {
        getStatePlaceholder()?.setPlaceholderType(type)
        getStatePlaceholder()?.visibility = View.VISIBLE
        getContentContainer()?.visibility = View.GONE
        getStatePlaceholder()?.setPlaceholderActionTip(tip)
    }

    /**
     * 把占位符隐藏，显示出指定的内容区域
     */
    fun showContentContainer() {
        getStatePlaceholder()?.visibility = View.GONE
        getContentContainer()?.visibility = View.VISIBLE
    }

    /**
     * 在最上层显示一个小弹窗以提示正在加载
     */
    protected open fun showLoading() {
        mLoading?.show()
    }

    protected open fun dismissLoading(task: Runnable) {
        mLoading?.dismissWith(task)
    }
}
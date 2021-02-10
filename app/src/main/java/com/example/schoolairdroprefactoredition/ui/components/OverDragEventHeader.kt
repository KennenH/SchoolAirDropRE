package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class OverDragEventHeader : View, RefreshHeader {

    private var mOnHeaderOverDragEventListener: OnHeaderOverDragEventListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        mOnHeaderOverDragEventListener?.onOverDragging(isDragging, percent, offset, height, maxDragHeight)
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        refreshLayout.finishRefresh()
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    interface OnHeaderOverDragEventListener {
        fun onOverDragging(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int)
    }

    fun setOnHeaderOverDragEventListener(listenerHeader: OnHeaderOverDragEventListener) {
        this.mOnHeaderOverDragEventListener = listenerHeader
    }
}

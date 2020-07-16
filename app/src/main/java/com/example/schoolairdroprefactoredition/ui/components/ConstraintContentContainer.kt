package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.effective.android.panel.view.content.ContentContainerImpl
import com.effective.android.panel.view.content.IContentContainer
import com.effective.android.panel.view.content.IInputAction
import com.effective.android.panel.view.content.IResetAction
import com.example.schoolairdroprefactoredition.R

class ConstraintContentContainer @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr), IContentContainer {
    @IdRes
    private var inputId = 0

    @IdRes
    private var resetViewID = 0
    private var autoResetByOnTouch: Boolean = true
    private lateinit var contentContainer: ContentContainerImpl

    init {
        initView(attrs, defStyleAttr, 0)
    }

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConstraintContentContainer, defStyleAttr, 0)
        inputId = typedArray.getResourceId(R.styleable.ConstraintContentContainer_input_view, -1)
        resetViewID = typedArray.getResourceId(R.styleable.ConstraintContentContainer_hide_by_touch_area, -1)
        autoResetByOnTouch = typedArray.getBoolean(R.styleable.ConstraintContentContainer_hide_by_touch_outside_enable, autoResetByOnTouch)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentContainer = ContentContainerImpl(this, autoResetByOnTouch, inputId, resetViewID)
    }

    override fun changeContainerHeight(targetHeight: Int) {
        contentContainer.changeContainerHeight(targetHeight)
    }

    override fun findTriggerView(id: Int): View? = contentContainer.findTriggerView(id)

    override fun getInputActionImpl(): IInputAction = contentContainer.getInputActionImpl()

    override fun getResetActionImpl(): IResetAction = contentContainer.getResetActionImpl()

    override fun layoutContainer(l: Int, t: Int, r: Int, b: Int) {
        contentContainer.layoutContainer(l, t, r, b)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val onTouchTrue = super.dispatchTouchEvent(ev)
        val hookResult = getResetActionImpl().hookDispatchTouchEvent(ev, onTouchTrue)
        return hookResult or onTouchTrue
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val onTouchBySelf = super.onTouchEvent(event)
        val hookResult = getResetActionImpl().hookOnTouchEvent(event)
        return onTouchBySelf or hookResult
    }
}
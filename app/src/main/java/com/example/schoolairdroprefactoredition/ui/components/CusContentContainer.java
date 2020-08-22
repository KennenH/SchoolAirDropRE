package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.effective.android.panel.interfaces.ContentScrollMeasurer;
import com.effective.android.panel.view.content.ContentContainerImpl;
import com.effective.android.panel.view.content.IContentContainer;
import com.effective.android.panel.view.content.IInputAction;
import com.effective.android.panel.view.content.IResetAction;
import com.example.schoolairdroprefactoredition.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CusContentContainer extends ConstraintLayout implements IContentContainer {
    @IdRes
    private int editTextId = 0;

    @IdRes
    private int resetViewId = 0;
    private boolean autoResetByOnTouch = true;
    private ContentContainerImpl contentContainer;

    public CusContentContainer(Context context) {
        super(context);
    }

    public CusContentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0, 0);
    }

    public CusContentContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr, 0);
    }

    private void initView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CusContentContainer, defStyleAttr, 0);
        editTextId = typedArray.getResourceId(R.styleable.CusContentContainer_input_view, -1);
        resetViewId = typedArray.getResourceId(R.styleable.CusContentContainer_hide_by_touch_area, -1);
        autoResetByOnTouch = typedArray.getBoolean(R.styleable.CusContentContainer_hide_by_touch_outside_enable, autoResetByOnTouch);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentContainer = new ContentContainerImpl(this, autoResetByOnTouch, editTextId, resetViewId);
    }

    @Override
    public void changeContainerHeight(int i) {
        contentContainer.changeContainerHeight(i);
    }

    @Nullable
    @Override
    public View findTriggerView(int i) {
        return contentContainer.findTriggerView(i);
    }

    @NotNull
    @Override
    public IInputAction getInputActionImpl() {
        return contentContainer.getInputActionImpl();
    }

    @NotNull
    @Override
    public IResetAction getResetActionImpl() {
        return contentContainer.getResetActionImpl();
    }

    @Override
    public void layoutContainer(int i, int i1, int i2, int i3, @NotNull List<ContentScrollMeasurer> list, int i4, boolean b, boolean b1) {
        contentContainer.layoutContainer(i, i1, i2, i3, list, i4, b, b1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchBySelf = super.onTouchEvent(event);
        boolean hookResult = getResetActionImpl().hookOnTouchEvent(event);
        return onTouchBySelf || hookResult;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean onTouchTrue = super.dispatchTouchEvent(ev);
        boolean hookResult = getResetActionImpl().hookDispatchTouchEvent(ev, onTouchTrue);
        return hookResult || onTouchTrue;
    }
}

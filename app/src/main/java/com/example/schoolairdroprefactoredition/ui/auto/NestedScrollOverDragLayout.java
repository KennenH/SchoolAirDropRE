package com.example.schoolairdroprefactoredition.ui.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;

import me.jessyan.autosize.AutoSizeCompat;

public class NestedScrollOverDragLayout extends OverDragLayout {
    public NestedScrollOverDragLayout(Context context) {
        this(context, null);
    }

    public NestedScrollOverDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        NestedScrollView scrollView = new NestedScrollView(getContext());
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(scrollView, params);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.generateLayoutParams(attrs);
    }
}

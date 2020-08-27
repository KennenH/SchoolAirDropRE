package com.example.schoolairdroprefactoredition.ui.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import me.jessyan.autosize.AutoSizeCompat;

public class OverDragLayout extends SmartRefreshLayout {
    public OverDragLayout(Context context) {
        super(context);
        init();
    }

    public OverDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEnableRefresh(false);
        setFooterMaxDragRate(1.2f);
        setHeaderMaxDragRate(1.4f);
        setEnableOverScrollDrag(true);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.generateLayoutParams(attrs);
    }
}

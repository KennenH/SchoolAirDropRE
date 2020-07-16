package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.example.schoolairdroprefactoredition.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
}

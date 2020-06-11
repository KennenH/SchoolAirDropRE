package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class SearchHistoryItem extends androidx.appcompat.widget.AppCompatTextView {
    public SearchHistoryItem(Context context) {
        this(context, null);
    }

    public SearchHistoryItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchHistoryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackground(getContext().getResources().getDrawable(R.drawable.item_search_history, getContext().getTheme()));
        setLines(1);
        setPadding(SizeUtils.dp2px(12), SizeUtils.dp2px(8), SizeUtils.dp2px(12), SizeUtils.dp2px(8));
        setEllipsize(TextUtils.TruncateAt.END);
    }
}

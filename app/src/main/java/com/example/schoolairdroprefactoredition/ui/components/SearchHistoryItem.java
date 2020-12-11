package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class SearchHistoryItem extends androidx.appcompat.widget.AppCompatTextView {
    public SearchHistoryItem(Context context, String key) {
        this(context, null, key);
    }

    public SearchHistoryItem(Context context, AttributeSet attrs, String key) {
        this(context, attrs, 0, key);
    }

    public SearchHistoryItem(Context context, AttributeSet attrs, int defStyleAttr, String key) {
        super(context, attrs, defStyleAttr);
        setLines(1);
        setText(key);
        setEllipsize(TextUtils.TruncateAt.END);
        setTextColor(context.getResources().getColor(R.color.primaryText, context.getTheme()));
        setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.item_search_history, getContext().getTheme()));
        setPadding(SizeUtils.dp2px(12), SizeUtils.dp2px(8), SizeUtils.dp2px(12), SizeUtils.dp2px(8));
    }
}

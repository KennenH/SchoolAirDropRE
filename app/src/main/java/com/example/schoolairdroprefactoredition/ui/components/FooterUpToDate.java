package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class FooterUpToDate extends AppCompatTextView {
    public FooterUpToDate(Context context) {
        this(context, null);
    }

    public FooterUpToDate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterUpToDate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int padding = SizeUtils.dp2px(8);
        setPadding(padding, padding, padding, padding);
        setGravity(Gravity.CENTER);
        setText(R.string.noMoreOlderData);
    }
}

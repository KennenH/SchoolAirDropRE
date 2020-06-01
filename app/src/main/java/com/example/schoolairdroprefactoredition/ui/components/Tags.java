package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.google.android.flexbox.FlexboxLayout;

public class Tags extends androidx.appcompat.widget.AppCompatTextView {
    public static final int ADVERTISEMENT = 0;// 广告
    public static final int SECOND_HAND = 1;// 二手
    public static final int NEGOTIABLE = 2;// 可议价
    public static final int FILL_RED = 3;// 全新

    public Tags(Context context, int type) {
        super(context);
        init(type);
    }

    public Tags(Context context, @Nullable AttributeSet attrs, int type) {
        super(context, attrs);
        init(type);
    }

    public Tags(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int type) {
        super(context, attrs, defStyleAttr);
        init(type);
    }

    private void init(int type) {
        switch (type) {
            case SECOND_HAND:
                setBackground(getContext().getResources().getDrawable(R.drawable.tags_primary_outlined, getContext().getTheme()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark, getContext().getTheme()));
                } else {
                    setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
                setText(R.string.second_hand);
                break;
            case ADVERTISEMENT:
                setBackground(getContext().getResources().getDrawable(R.drawable.tags_grey_outlined, getContext().getTheme()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTextColor(getContext().getResources().getColor(R.color.primaryDarkest, getContext().getTheme()));
                } else {
                    setTextColor(getContext().getResources().getColor(R.color.primaryDarkest));
                }
                setText(R.string.advertisement);
                break;
            case NEGOTIABLE:
                setBackground(getContext().getResources().getDrawable(R.drawable.tags_green_filled, getContext().getTheme()));
                setTextColor(Color.WHITE);
                setText(R.string.negotiable);
                break;
            case FILL_RED:
                setBackground(getContext().getResources().getDrawable(R.drawable.tags_red_filled, getContext().getTheme()));
                setTextColor(Color.WHITE);
                break;
        }
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        setGravity(Gravity.CENTER);
        int paddingH = SizeUtils.dp2px(5);
        int paddingV = SizeUtils.dp2px(3);
        params.setMargins(paddingV, paddingV, paddingV, paddingV);
        setLayoutParams(params);
        setPadding(paddingH, paddingV, paddingH, paddingV);
    }
}

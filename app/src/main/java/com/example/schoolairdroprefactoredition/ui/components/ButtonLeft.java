package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.schoolairdroprefactoredition.R;

public class ButtonLeft extends LinearLayout {

    private OnButtonClickListener mOnButtonClickListener;

    public ButtonLeft(Context context) {
        this(context, null);
    }

    public ButtonLeft(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonLeft(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_button_left, this, true);

        final ImageView mButtonOne = findViewById(R.id.button_one);
        mButtonOne.setOnClickListener(v -> {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.onLeftButtonClick();
        });
    }

    public interface OnButtonClickListener {
        void onLeftButtonClick();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}

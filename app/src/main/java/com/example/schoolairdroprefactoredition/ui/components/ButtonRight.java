package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.schoolairdroprefactoredition.R;

public class ButtonRight extends LinearLayout {

    private static final int FAVORED = R.drawable.favorite_red;
    private static final int UNFAVORED = R.drawable.outline_favorite_24;

    private final ImageView mButtonLeft;
//    private final ImageView mButtonRight;
    private OnButtonClickListener mOnButtonClickListener;

    private boolean isFavored = false;

    public ButtonRight(Context context) {
        this(context, null);
    }

    public ButtonRight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_button_right, this, true);
        mButtonLeft = findViewById(R.id.button_one);
//        mButtonRight = findViewById(R.id.button_two);
        mButtonLeft.setOnClickListener(v -> {
            if (mOnButtonClickListener != null) {
                mOnButtonClickListener.onRightButtonClick();
            }
        });
//        mButtonRight.setOnClickListener(v -> {
//            if (mOnButtonClickListener != null) {
//                mOnButtonClickListener.onRightButtonClick();
//            }
//        });
    }

    public void setFavor(boolean favor) {
        isFavored = favor;
        if (favor) {
            mButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), FAVORED));
        } else {
            mButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), UNFAVORED));
        }
    }

    public void toggleFavor() {
        mButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), isFavored ? UNFAVORED : FAVORED));
        isFavored = !isFavored;
    }

    public interface OnButtonClickListener {
        void onRightButtonClick();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}

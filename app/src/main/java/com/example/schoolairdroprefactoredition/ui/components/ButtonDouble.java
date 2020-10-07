package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.schoolairdroprefactoredition.R;

public class ButtonDouble extends LinearLayout {

    private ImageView mButtonOne;
    private ImageView mButtonTwo;

    private OnButtonClickListener mOnButtonClickListener;

    public ButtonDouble(Context context) {
        this(context, null);
    }

    public ButtonDouble(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonDouble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_button_right, this, true);

        mButtonOne = findViewById(R.id.button_one);
        mButtonTwo = findViewById(R.id.button_two);
        mButtonOne.setOnClickListener(v -> {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.onLeftButtonClick();
        });
        mButtonTwo.setOnClickListener(v -> {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.onRightButtonClick();
        });
    }

    /**
     * must be drawable
     * @param res drawable
     */
    public void setImageLeft(int res) {
        mButtonOne.setImageResource(res);
    }

    /**
     * must be drawable
     * @param res drawable
     */
    public void setImageRight(int res) {
        mButtonTwo.setImageResource(res);
    }

    public interface OnButtonClickListener {
        void onLeftButtonClick();

        void onRightButtonClick();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}

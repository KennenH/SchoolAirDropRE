package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class ButtonSingle extends LinearLayout {

    private ImageView mButtonOne;

    private OnButtonClickListener mOnButtonClickListener;

    public ButtonSingle(Context context) {
        this(context, null);
    }

    public ButtonSingle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonSingle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_button_left, this, true);

        int padding = SizeUtils.dp2px(15);
        mButtonOne = findViewById(R.id.button_one);
        mButtonOne.setPadding(padding, padding, padding, padding);
        mButtonOne.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.chat, getContext().getTheme()));
        mButtonOne = findViewById(R.id.button_one);
        mButtonOne.setOnClickListener(v -> {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.onButtonClick();
        });
    }

    /**
     * must be drawable
     *
     * @param res drawable res
     */
    public void setImage(int res) {
        mButtonOne.setImageResource(res);
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}

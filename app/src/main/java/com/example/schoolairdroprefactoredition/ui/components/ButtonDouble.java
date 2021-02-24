package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.schoolairdroprefactoredition.R;
import com.github.ybq.android.spinkit.SpinKitView;

public class ButtonDouble extends LinearLayout {

    private static final int FAVORED = R.drawable.favorite_red;
    private static final int UNFAVORED = R.drawable.outline_favorite_24;

    private final ImageView mButtonLeft;
    private final ImageView mButtonRight;
    private final SpinKitView mLoading;

    private OnButtonClickListener mOnButtonClickListener;

    private boolean isLoading = false;

    private boolean isFavored = false;

    public ButtonDouble(Context context) {
        this(context, null);
    }

    public ButtonDouble(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonDouble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.component_goods_button_right, this, true);

        mLoading = findViewById(R.id.loading);
        mButtonLeft = findViewById(R.id.button_one);
        mButtonRight = findViewById(R.id.button_two);
        mButtonLeft.setOnClickListener(v -> {
            if (!isLoading && mOnButtonClickListener != null)
                mOnButtonClickListener.onLeftButtonClick();
        });
        mButtonRight.setOnClickListener(v -> {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.onRightButtonClick();
        });
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

    public boolean getIsFavored() {
        return isFavored;
    }

    /**
     * must be drawable
     *
     * @param drawable drawable
     */
    public void setImageRight(Drawable drawable) {
        mButtonRight.setImageDrawable(drawable);
    }

    /**
     * 使左边的按钮进入加载状态
     */
    public void setButtonLeftLoading(boolean loading) {
        if (loading) {
            if (isLoading) return;

            isLoading = true;
            mButtonLeft.setVisibility(INVISIBLE);
            mLoading.setVisibility(VISIBLE);
        } else {
            if (!isLoading) return;

            isLoading = false;
            mButtonLeft.setVisibility(VISIBLE);
            mLoading.setVisibility(INVISIBLE);
        }
    }

    public interface OnButtonClickListener {
        void onLeftButtonClick();

        void onRightButtonClick();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}

package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.auto.ConstrainLayoutAuto;

public class ErrorPlaceHolder extends ConstrainLayoutAuto implements View.OnClickListener {
    public static final int TYPE_ERROR = 88;
    public static final int TYPE_EMPTY = 99;

    private static final int ICON_ERROR = R.drawable.ic_error;
    private static final int ICON_EMPTY = R.drawable.ic_empty;

    private static final int TIP_ERROR = R.string.errorNetLocation;
    private static final int TIP_EMPTY = R.string.errorEmpty;

    private static final int ACT_RETRY = R.string.errorRetry;
    private static final int ACT_EMPTY = R.string.errorEmpty;


    private ImageView mIcon;
    private TextView mTip;
    private TextView mAction;

    private OnPlaceHolderActionListener mOnPlaceHolderActionListener;

    public ErrorPlaceHolder(Context context) {
        this(context, null);
    }

    public ErrorPlaceHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorPlaceHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.placeholder_location, this, true);
        mIcon = findViewById(R.id.imageView);
        mTip = findViewById(R.id.textView);
        mAction = findViewById(R.id.button);

        mAction.setOnClickListener(this);
    }

    /**
     * 设置placeholder类型
     *
     * @param type {@link ErrorPlaceHolder#TYPE_EMPTY}{@link ErrorPlaceHolder#TYPE_ERROR}
     */
    public void setPlaceHolderType(int type) {
        if (type != TYPE_EMPTY && type != TYPE_ERROR)
            throw new IllegalArgumentException("no such type of placeholder");

        if (type == TYPE_ERROR) {
            mIcon.setImageResource(ICON_ERROR);
            mTip.setText(TIP_ERROR);
            mAction.setText(ACT_RETRY);
        } else if (type == TYPE_EMPTY) {
            mIcon.setImageResource(ICON_EMPTY);
            mTip.setText(TIP_EMPTY);
            mAction.setText(ACT_EMPTY);
        }
    }

    public interface OnPlaceHolderActionListener {
        void onPlaceHolderAction(View view);
    }

    public void setOnPlaceHolderActionListener(OnPlaceHolderActionListener listener) {
        mOnPlaceHolderActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button) {
            if (mOnPlaceHolderActionListener != null)
                mOnPlaceHolderActionListener.onPlaceHolderAction(v);

        }
    }
}

package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class ArrangePositionHeader extends ConstraintLayout implements View.OnClickListener {

    private TextView mDistrict;
    private TextView mRelocate;

    private OnRelocateListener mOnRelocateListener;

    public ArrangePositionHeader(Context context) {
        this(context, null);
    }

    public ArrangePositionHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrangePositionHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_position_header, this, true);

        mDistrict = findViewById(R.id.district);
        mRelocate = findViewById(R.id.relocate);

        mRelocate.setOnClickListener(this);
    }

    public void setDistrict(String district) {
        mDistrict.setText(district);
    }

    public interface OnRelocateListener {
        void onRelocate(View view);
    }

    public void setOnRelocateListener(OnRelocateListener listener) {
        mOnRelocateListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.relocate) {
            if (mOnRelocateListener != null)
                mOnRelocateListener.onRelocate(v);
        }
    }
}

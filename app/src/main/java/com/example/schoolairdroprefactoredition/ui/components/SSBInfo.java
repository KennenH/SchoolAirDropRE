package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentSsbBinding;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

public class SSBInfo extends ConstraintLayoutAuto implements View.OnClickListener {

    private OnSSBActionListener mOnSSBActionListener;

    public SSBInfo(Context context) {
        this(context, null);
    }

    public SSBInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSBInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ComponentSsbBinding binding = ComponentSsbBinding.bind(LayoutInflater.from(context).inflate(R.layout.component_ssb, this, true));

        binding.mySellingWrapper.setOnClickListener(this);
        binding.myPostsWrapper.setOnClickListener(this);
    }

    public interface OnSSBActionListener {
        void onSellingClick(View view);

        void onPostsClick(View view);
    }

    public void setOnSSBActionListener(OnSSBActionListener listener) {
        this.mOnSSBActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.my_selling_wrapper) {
            if (mOnSSBActionListener != null) {
                mOnSSBActionListener.onSellingClick(v);
            }
        } else if (id == R.id.my_posts_wrapper) {
            if (mOnSSBActionListener != null) {
                mOnSSBActionListener.onPostsClick(v);
            }
        }
    }
}

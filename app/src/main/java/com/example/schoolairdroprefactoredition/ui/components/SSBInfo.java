package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentSsbBinding;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

public class SSBInfo extends ConstraintLayoutAuto implements View.OnClickListener {

    private ComponentSsbBinding binding;

    private OnSSBActionListener mOnSSBActionListener;

    public SSBInfo(Context context) {
        this(context, null);
    }

    public SSBInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSBInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentSsbBinding.bind(LayoutInflater.from(context).inflate(R.layout.component_ssb, this, true));
        binding.mySellingWrapper.setOnClickListener(this);
        binding.mySoldWrapper.setOnClickListener(this);
        binding.myBoughtWrapper.setOnClickListener(this);
    }

    public interface OnSSBActionListener {
        void onSellingClick(View view);

        void onSoldClick(View view);

        void onBoughtClick(View view);
    }

    public void setOnSSBActionListener(OnSSBActionListener listener) {
        this.mOnSSBActionListener = listener;
    }

//    public void setSelling(int selling) {
//        binding.mySelling.setText(String.valueOf(selling));
//    }
//
//    public void setSold(int sold) {
//        binding.mySold.setText(String.valueOf(sold));
//    }
//
//    public void setBought(int bought) {
//        binding.myBought.setText(String.valueOf(bought));
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_selling_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onSellingClick(v);
                break;
            case R.id.my_sold_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onSoldClick(v);
                break;
            case R.id.my_bought_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onBoughtClick(v);
                break;
        }
    }
}

package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class SSBInfo extends ConstraintLayout implements View.OnClickListener {

    private TextView mSelling;
    private TextView mSold;
    private TextView mBought;

    private TextView mSellingT;
    private TextView mSoldT;
    private TextView mBoughtT;

    private OnSSBActionListener mOnSSBActionListener;

    public SSBInfo(Context context) {
        this(context, null);
    }

    public SSBInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSBInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.component_ssb, this, true);
        mSelling = findViewById(R.id.my_selling);
        mSold = findViewById(R.id.my_sold);
        mBought = findViewById(R.id.my_bought);
        mSellingT = findViewById(R.id.my_selling_t);
        mSoldT = findViewById(R.id.my_sold_t);
        mBoughtT = findViewById(R.id.my_bought_t);

        mSelling.setOnClickListener(this);
        mSold.setOnClickListener(this);
        mBought.setOnClickListener(this);
        mSellingT.setOnClickListener(this);
        mSoldT.setOnClickListener(this);
        mBoughtT.setOnClickListener(this);
        findViewById(R.id.my_selling_wrapper).setOnClickListener(this);
        findViewById(R.id.my_sold_wrapper).setOnClickListener(this);
        findViewById(R.id.my_bought_wrapper).setOnClickListener(this);
    }

    public void setSelling(int selling) {
        mSelling.setText(NumberUtil.num2StringWithUnit(selling));
    }

    public void setSold(int sold) {
        mSold.setText(NumberUtil.num2StringWithUnit(sold));
    }

    public void setBought(int bought) {
        mBought.setText(NumberUtil.num2StringWithUnit(bought));
    }


    public interface OnSSBActionListener {
        void onSellingClick(View view);

        void onSoldClick(View view);

        void onBoughtClick(View view);
    }

    public void setOnSSBActionListener(OnSSBActionListener listener) {
        this.mOnSSBActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_selling:
            case R.id.my_selling_t:
            case R.id.my_selling_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onSellingClick(v);
                break;
            case R.id.my_sold:
            case R.id.my_sold_t:
            case R.id.my_sold_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onSoldClick(v);
                break;
            case R.id.my_bought:
            case R.id.my_bought_t:
            case R.id.my_bought_wrapper:
                if (mOnSSBActionListener != null)
                    mOnSSBActionListener.onBoughtClick(v);
                break;
        }
    }
}

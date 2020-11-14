package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class GoodsPrice extends ConstraintLayout {

    private final TextView mCurrency;
    private final TextView mPriceInt;
    private final TextView mPriceFraction;

    public GoodsPrice(Context context) {
        this(context, null);
    }

    public GoodsPrice(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsPrice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_price_light, this, true);

        mCurrency = findViewById(R.id.price_currency);
        mPriceInt = findViewById(R.id.price_int);
        mPriceFraction = findViewById(R.id.price_fraction);
    }

    public void setPrice(float price) {
        String p = NumberUtil.num2Money(price);
        int dot = p.indexOf(".");
        mCurrency.setText(R.string.currency_RMB);
        if (dot != -1) {
            mPriceInt.setText(p.substring(0, dot));
            mPriceFraction.setText(p.substring(dot));
        } else {
            mPriceInt.setText(p);
            mPriceFraction.setText(R.string.fraction0);
        }
    }

    public void setPrice(String price) {
        if (price == null) return;
        float priceF = Float.parseFloat(price);
        String p = NumberUtil.num2Money(priceF);
        int dot = p.indexOf(".");

        mCurrency.setText(R.string.currency_RMB);
        if (dot != -1) {
            mPriceInt.setText(p.substring(0, dot));
            mPriceFraction.setText(p.substring(dot));
        } else {
            mPriceInt.setText(p);
            mPriceFraction.setText(R.string.fraction0);
        }
    }
}

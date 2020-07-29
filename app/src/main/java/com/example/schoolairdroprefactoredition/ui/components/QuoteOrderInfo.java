package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteDetailBean;

import java.util.Calendar;

public class QuoteOrderInfo extends ConstraintLayout {

    private Context mContext;
    private TextView mID;
    private TextView mCreation;
    private TextView mExpiration;

    public QuoteOrderInfo(Context context) {
        this(context, null);
    }

    public QuoteOrderInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteOrderInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_order_info, this, true);

        mContext = context;
        mID = findViewById(R.id.order_id);
        mCreation = findViewById(R.id.order_creation);
        mExpiration = findViewById(R.id.order_expiration);
    }

    public void setOrderInfo(TestQuoteDetailBean data) {
        mID.setText(mContext.getResources().getString(R.string.orderID, data.getOrderID()));
        mCreation.setText(mContext.getResources().getString(R.string.orderCreation, data.getCreateTime().get(Calendar.YEAR), data.getCreateTime().get(Calendar.MONTH) + 1, data.getCreateTime().get(Calendar.DAY_OF_MONTH)));
        mExpiration.setText(mContext.getResources().getString(R.string.orderExpiration, data.getExpirationTime().get(Calendar.YEAR), data.getExpirationTime().get(Calendar.MONTH) + 1, data.getExpirationTime().get(Calendar.DAY_OF_MONTH)));
    }
}

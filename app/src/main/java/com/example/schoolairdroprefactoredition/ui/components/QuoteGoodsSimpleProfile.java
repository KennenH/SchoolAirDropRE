package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteDetailBean;

public class QuoteGoodsSimpleProfile extends ConstraintLayout {

    private Context mContext;

    private TextView mTitle;
    private ImageView mAvatar;
    private TextView mPrice;

    public QuoteGoodsSimpleProfile(Context context) {
        this(context, null);
    }

    public QuoteGoodsSimpleProfile(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteGoodsSimpleProfile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_quote_goods_profile, this, true);

        mContext = context;
        mTitle = findViewById(R.id.goods_info_name);
        mAvatar = findViewById(R.id.goods_info_avatar);
        mPrice = findViewById(R.id.goods_info_price);
    }

    public void setGoodsProfile(TestQuoteDetailBean data) {
        mTitle.setText(data.getTitle());
        mPrice.setText(mContext.getResources().getString(R.string.priceRMB, data.getOriginPrice()));

    }
}

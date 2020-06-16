package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolairdroprefactoredition.R;

public class RecyclerItem extends ConstraintLayout {

    private ImageView mImage;
    private TextView mTitle;
    private GoodsPrice mGoodsPrice;

    public RecyclerItem(Context context) {
        this(context, null);
    }

    public RecyclerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_home_goods_info, this, true);

        mImage = findViewById(R.id.item_image);
        mTitle = findViewById(R.id.item_title);
        mGoodsPrice = findViewById(R.id.item_price);
    }

    public void setImage(String url) {
        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .into(mImage);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setPrice(float price) {
        mGoodsPrice.setPrice(price);
    }

}

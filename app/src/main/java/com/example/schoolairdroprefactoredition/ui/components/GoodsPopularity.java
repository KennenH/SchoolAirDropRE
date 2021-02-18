package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class GoodsPopularity extends ConstraintLayout {

    private final TextView mComments;
    private final TextView mLikes;
    private final TextView mWatches;

    public GoodsPopularity(Context context) {
        this(context, null);
    }

    public GoodsPopularity(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsPopularity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_popularity, this, true);

        mComments = findViewById(R.id.item_comments);
        mLikes = findViewById(R.id.item_likes);
        mWatches = findViewById(R.id.item_watches);
    }

    public void setComments(int comments) {
        mComments.setText(NumberUtil.num2StringWithUnit(comments));
    }

    public void setLikes(int likes) {
        mLikes.setText(NumberUtil.num2StringWithUnit(likes));
    }

    public void setWatches(int watches) {
        mWatches.setText(NumberUtil.num2StringWithUnit(watches));
    }
}

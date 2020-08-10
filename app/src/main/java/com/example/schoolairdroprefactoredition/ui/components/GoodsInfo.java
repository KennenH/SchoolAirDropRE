package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsDetailBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexboxLayout;

public class GoodsInfo extends ShimmerFrameLayout {

    private TextView mGoodsName;
    private FlexboxLayout mTags;
    private GoodsPopularity mPopularity;
    private GoodsPrice mPrice;
    private SimpleDraweeView mAvatar;
    private TextView mUserName;
    private TextView mDescription;

    public GoodsInfo(Context context) {
        this(context, null);
    }

    public GoodsInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_detail, this, true);

        mGoodsName = findViewById(R.id.goods_name);
        mTags = findViewById(R.id.goods_tags);
        mPopularity = findViewById(R.id.goods_popularity);
        mPrice = findViewById(R.id.goods_price);
        mAvatar = findViewById(R.id.goods_avatar);
        mUserName = findViewById(R.id.goods_user_name);
        mDescription = findViewById(R.id.goods_description);
    }

    public void setData(DomainGoodsInfo.GoodsInfoBean data) {
        if (data != null) {
            mGoodsName.setText(data.getTital());
            mTags.removeAllViews();
//        for (Integer tag : data.getTags()) {
//            mTags.addView(new Tags(getContext(), tag));
//        }
//        mPopularity.setComments(data.get());
//        mPopularity.setLikes(data.getLikes());
//        mPopularity.setWatches(data.getWatches());
            mPrice.setPrice(data.getPrice());
//        mAvatar.setImageURI(data.getAvatar());
            mUserName.setText("校园空投官方");
            mDescription.setText(data.getDescription());
            stopShimmer();
            hideShimmer();
        }
    }
}

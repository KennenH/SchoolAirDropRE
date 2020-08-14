package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;

public class GoodsInfo extends ShimmerFrameLayout {

    private TextView mGoodsName;
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
        mPopularity = findViewById(R.id.goods_popularity);
        mPrice = findViewById(R.id.goods_price);
        mAvatar = findViewById(R.id.goods_avatar);
        mUserName = findViewById(R.id.goods_user_name);
        mDescription = findViewById(R.id.goods_description);
    }

    public void setData(DomainGoodsInfo.GoodsInfoBean data) {
        if (data != null) {
            boolean negotiable = data.getIsPrice().equals("1");// 是否可议价
            boolean secondHand = data.getIstender().equals("1");// 是否二手
            if (negotiable && secondHand)
                mGoodsName.setText(getContext().getResources().getString(R.string.itemNS, data.getTitle()));
            else if (negotiable)
                mGoodsName.setText(getContext().getResources().getString(R.string.itemN, data.getTitle()));
            else if (secondHand)
                mGoodsName.setText(getContext().getResources().getString(R.string.itemS, data.getTitle()));
            else
                mGoodsName.setText(data.getTitle());

//        mPopularity.setComments(data.get());
//        mPopularity.setLikes(data.getLikes());
//        mPopularity.setWatches(data.getWatches());
            mPrice.setPrice(data.getPrice());
//        mAvatar.setImageURI(data.getAvatar());
            mUserName.setText(data.getUname());
            mDescription.setText(data.getDescription());
            stopShimmer();
            hideShimmer();
        }
    }
}

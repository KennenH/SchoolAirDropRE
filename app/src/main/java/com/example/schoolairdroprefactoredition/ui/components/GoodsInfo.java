package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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

    public void setData(DomainGoodsInfo.DataBean data) {
        if (data != null) {
            try {
                boolean negotiable = data.getGoods_is_quotable() == 1;// 是否可议价
                boolean secondHand = data.getGoods_is_brandNew() == 0;// 是否二手
                if (negotiable && secondHand)
                    mGoodsName.setText(getContext().getResources().getString(R.string.itemNS, data.getGoods_name()));
                else if (negotiable)
                    mGoodsName.setText(getContext().getResources().getString(R.string.itemN, data.getGoods_name()));
                else if (secondHand)
                    mGoodsName.setText(getContext().getResources().getString(R.string.itemS, data.getGoods_name()));
                else
                    mGoodsName.setText(data.getGoods_name());

                mPrice.setPrice(data.getGoods_price());
                mAvatar.setImageURI(data.getSeller_info().getUser_img_path());
                mUserName.setText(data.getSeller_info().getUname());
                mDescription.setText(data.getGoods_description());
            } catch (NullPointerException e) {
                Log.d("GoodsInfo", "null pointer exception");
            } finally {
                stopShimmer();
                hideShimmer();
            }
        }
    }
}

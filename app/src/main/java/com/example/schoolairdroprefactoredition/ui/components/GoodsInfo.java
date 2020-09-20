package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsDetailBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.facebook.shimmer.ShimmerFrameLayout;

public class GoodsInfo extends ShimmerFrameLayout {

    private ComponentGoodsDetailBinding binding;

    public GoodsInfo(Context context) {
        this(context, null);
    }

    public GoodsInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentGoodsDetailBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setData(DomainGoodsInfo.DataBean data) {
        if (data != null) {
            try {
                boolean negotiable = data.getGoods_is_quotable() == 1;// 是否可议价
                boolean secondHand = data.getGoods_is_brandNew() == 0;// 是否二手
                if (negotiable && secondHand)
                    binding.goodsName.setText(getContext().getResources().getString(R.string.itemNS, data.getGoods_name()));
                else if (negotiable)
                    binding.goodsName.setText(getContext().getResources().getString(R.string.itemN, data.getGoods_name()));
                else if (secondHand)
                    binding.goodsName.setText(getContext().getResources().getString(R.string.itemS, data.getGoods_name()));
                else
                    binding.goodsName.setText(data.getGoods_name());

                binding.goodsPrice.setPrice(data.getGoods_price());
                binding.goodsAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + data.getSeller_info().getUser_img_path());
                binding.goodsUserName.setText(data.getSeller_info().getUname());
                binding.goodsDescription.setText(data.getGoods_description());
            } catch (NullPointerException e) {
                LogUtils.d("goods info null");
            } finally {
                stopShimmer();
                hideShimmer();
            }
        }
    }
}

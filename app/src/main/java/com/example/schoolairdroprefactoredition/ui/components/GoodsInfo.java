package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsDetailBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.facebook.shimmer.ShimmerFrameLayout;

public class GoodsInfo extends ShimmerFrameLayout implements View.OnClickListener {

    private final ComponentGoodsDetailBinding binding;

    private OnUserInfoClickListener mOnUserInfoClickListener;

    public GoodsInfo(Context context) {
        this(context, null);
    }

    public GoodsInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentGoodsDetailBinding.inflate(LayoutInflater.from(context), this, true);

        binding.goodsAvatar.setOnClickListener(this);
        binding.goodsUserName.setOnClickListener(this);

        binding.goodsBottom.setVisibility(GONE);
    }

    /**
     * 是否隐藏页面最底部为三个按钮的留白
     * 当为自己的物品时，三个按钮隐藏，留白也应该隐藏
     */
    public void showBottom() {
        binding.goodsBottom.setVisibility(VISIBLE);
    }

    /**
     * 隐藏卖家信息
     * 从在售页面进入的物品界面要隐藏卖家信息
     * 防止在查看他人在售列表时
     * 点击物品 -> 查看用户信息 -> 点击在售 -> 再点击物品…
     * 无限套娃访问
     */
    public void hideSellerInfo() {
        binding.goodsSellerInfo.setVisibility(GONE);
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
                binding.goodsDescription.setText(data.getGoods_description());
                binding.goodsPager.setData(MyUtil.getArrayFromString(data.getGoods_img_set()));

                if (data.getSeller_info() != null) {
                    ImageUtil.loadRoundedImage(binding.goodsAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + data.getSeller_info().getUser_img_path());
                    binding.goodsUserName.setText(data.getSeller_info().getUname());
                } else binding.goodsSellerInfo.setVisibility(GONE);
            } catch (NullPointerException ignored) {
            }
        }
    }

    /**
     * 若仍在闪烁则停止闪烁
     */
    public void stopShimming() {
        if (isShimmerVisible() || isShimmerStarted()) {
            stopShimmer();
            hideShimmer();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goodsAvatar || id == R.id.goods_user_name) {
            if (mOnUserInfoClickListener != null)
                mOnUserInfoClickListener.onUserInfoClick(v);
        }
    }

    public interface OnUserInfoClickListener {
        void onUserInfoClick(View view);
    }

    public void setOnUserInfoClickListener(OnUserInfoClickListener listener) {
        mOnUserInfoClickListener = listener;
    }
}

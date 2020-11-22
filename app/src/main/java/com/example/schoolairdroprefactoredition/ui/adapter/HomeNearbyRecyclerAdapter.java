package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemHomeGoodsInfoBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

/**
 * 附近在售列表的adapter
 */
public class HomeNearbyRecyclerAdapter extends BaseFooterAdapter<DomainGoodsInfo.DataBean, BaseViewHolder> {

    public HomeNearbyRecyclerAdapter() {
        super(R.layout.item_home_goods_info);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.DataBean data) {
        if (data != null) {
            try {
                final ItemHomeGoodsInfoBinding binding = ItemHomeGoodsInfoBinding.bind(holder.itemView);
                boolean negotiable = data.getGoods_is_quotable() == 1;
                boolean secondHand = data.getGoods_is_brandNew() == 0;

                ImageUtil.loadRoundedImage(binding.itemImage, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + data.getGoods_img_cover());
                binding.itemPrice.setPrice(data.getGoods_price());
                binding.itemSeller.setText(data.getSeller_info().getUname());

                if (getContext() instanceof MainActivity) {
                    Bundle bundle = ((MainActivity) getContext()).getIntent().getExtras();
                    binding.getRoot().setOnClickListener(v -> GoodsActivity.Companion.start(getContext(),
                            (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE),
                            data, false));
                }

                if (negotiable && secondHand)
                    binding.itemTitle.setText(getContext().getResources().getString(R.string.itemNSs, data.getGoods_name()));
                else if (negotiable)
                    binding.itemTitle.setText(getContext().getResources().getString(R.string.itemNs, data.getGoods_name()));
                else if (secondHand)
                    binding.itemTitle.setText(getContext().getResources().getString(R.string.itemSs, data.getGoods_name()));
                else
                    binding.itemTitle.setText(data.getGoods_name());

                int creditNum = data.getSeller_info().getCredit_num();

                if (creditNum != 0)
                    if (creditNum == 5)
                        binding.itemCredit.setText(R.string.ic_credit5);
                    else if (creditNum == 4)
                        binding.itemCredit.setText(R.string.ic_credit4);
                    else if (creditNum == 3)
                        binding.itemCredit.setText(R.string.ic_credit3);
                    else if (creditNum == 2)
                        binding.itemCredit.setText(R.string.ic_credit2);
                    else if (creditNum == 1)
                        binding.itemCredit.setText(R.string.ic_credit1);
            } catch (NullPointerException e) {
            }
        }
    }

}
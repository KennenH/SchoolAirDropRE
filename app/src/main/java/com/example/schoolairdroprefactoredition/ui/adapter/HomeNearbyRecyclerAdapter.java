package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPrice;
import com.example.schoolairdroprefactoredition.ui.components.TextViewWithImages;
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
                boolean negotiable = data.getGoods_is_quotable() == 1;
                boolean secondHand = data.getGoods_is_brandNew() == 0;

                TextViewWithImages title = holder.itemView.findViewById(R.id.item_title);
                TextView credit = holder.itemView.findViewById(R.id.item_credit);

                ImageUtil.roundedImageLoad(holder.itemView.findViewById(R.id.item_image), ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + data.getGoods_img_cover(), SizeUtils.dp2px(8));
                ((GoodsPrice) holder.findView(R.id.item_price)).setPrice(data.getGoods_price());
                holder.setText(R.id.item_seller, data.getSeller_info().getUname());

                if (getContext() instanceof MainActivity) {
                    Bundle bundle = ((MainActivity) getContext()).getBundle();
                    holder.itemView.setOnClickListener(v -> GoodsActivity.start(getContext(), bundle, data));
                }

                if (negotiable && secondHand)
                    title.setText(getContext().getResources().getString(R.string.itemNSs, data.getGoods_name()));
                else if (negotiable)
                    title.setText(getContext().getResources().getString(R.string.itemNs, data.getGoods_name()));
                else if (secondHand)
                    title.setText(getContext().getResources().getString(R.string.itemSs, data.getGoods_name()));
                else
                    title.setText(data.getGoods_name());

                int creditNum = data.getSeller_info().getCredit_num();

                if (creditNum != 0)
                    if (creditNum == 5)
                        credit.setText(R.string.ic_credit5);
                    else if (creditNum == 4)
                        credit.setText(R.string.ic_credit4);
                    else if (creditNum == 3)
                        credit.setText(R.string.ic_credit3);
                    else if (creditNum == 2)
                        credit.setText(R.string.ic_credit2);
                    else if (creditNum == 1)
                        credit.setText(R.string.ic_credit1);
            } catch (NullPointerException e) {
                Log.d("NeaByRecyclerAdapter", "null pointer exception" + e.toString());
            }
        }
    }
}
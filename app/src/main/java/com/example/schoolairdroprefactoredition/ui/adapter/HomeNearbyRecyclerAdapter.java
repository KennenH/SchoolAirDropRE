package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPrice;
import com.example.schoolairdroprefactoredition.ui.components.TextViewWithImages;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

/**
 * 附近在售列表的adapter
 */
public class HomeNearbyRecyclerAdapter extends BaseQuickAdapter<DomainGoodsInfo.GoodsInfoBean, BaseViewHolder> implements View.OnClickListener {
    public HomeNearbyRecyclerAdapter() {
        super(R.layout.item_home_goods_info);
    }

    private DomainGoodsInfo.GoodsInfoBean info;

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.GoodsInfoBean data) {
        if (data != null) {
            info = data;
            boolean negotiable = data.getIsPrice().equals("1");
            boolean secondHand = data.getIstender().equals("1");

            ((SimpleDraweeView) holder.findView(R.id.item_image)).setImageURI(data.getCover());
            ((GoodsPrice) holder.findView(R.id.item_price)).setPrice(data.getPrice());
            holder.setText(R.id.item_seller, data.getUname());
            TextViewWithImages title = holder.findView(R.id.item_title);
            ImageView credit = holder.findView(R.id.item_credit);
            holder.itemView.setOnClickListener(this);

            if (negotiable && secondHand)
                title.setText(getContext().getResources().getString(R.string.itemNSs, data.getTitle()));
            else if (negotiable)
                title.setText(getContext().getResources().getString(R.string.itemNs, data.getTitle()));
            else if (secondHand)
                title.setText(getContext().getResources().getString(R.string.itemSs, data.getTitle()));
            else
                title.setText(data.getTitle());

            String creditNum = data.getCredit_num();

            if (creditNum != null)
                if (creditNum.equals("5"))
                    credit.setImageResource(R.drawable.ic_credit5);
                else if (creditNum.equals("4"))
                    credit.setImageResource(R.drawable.ic_credit4);
                else if (creditNum.equals("3"))
                    credit.setImageResource(R.drawable.ic_credit3);
                else if (creditNum.equals("2"))
                    credit.setImageResource(R.drawable.ic_credit2);
                else if (creditNum.equals("1"))
                    credit.setImageResource(R.drawable.ic_credit1);
        }
    }

    @Override
    public void onClick(View v) {
        GoodsActivity.start(getContext(), info);
    }
}
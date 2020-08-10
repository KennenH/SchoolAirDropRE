package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
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
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.GoodsInfoBean item) {
        if (item != null) {
            info = item;
            ((SimpleDraweeView) holder.findView(R.id.item_image)).setImageURI(item.getCover());
            holder.setText(R.id.item_title, item.getTital());
            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        GoodsActivity.start(getContext(), info);
    }
}
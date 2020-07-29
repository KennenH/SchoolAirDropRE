package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import org.jetbrains.annotations.NotNull;

/**
 * 附近在售列表的adapter
 */
public class HomeNearbyRecyclerAdapter extends BaseQuickAdapter<TestGoodsItemBean, BaseViewHolder> implements View.OnClickListener {
    public HomeNearbyRecyclerAdapter() {
        super(R.layout.item_home_goods_info);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestGoodsItemBean item) {
        if (item != null) {
            Glide.with(getContext())
                    .load(item.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(getContext().getResources().getDrawable(R.drawable.logo_placeholder, getContext().getTheme()))
                    .dontTransform()
                    .into((ImageView) holder.itemView.findViewById(R.id.item_image));

            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GoodsActivity.class);
        getContext().startActivity(intent);
    }
}
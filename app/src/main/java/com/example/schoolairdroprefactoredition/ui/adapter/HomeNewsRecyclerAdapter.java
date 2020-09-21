package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ClickUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

import org.jetbrains.annotations.NotNull;

public class HomeNewsRecyclerAdapter extends BaseMultiItemQuickAdapter<BaseHomeNewsEntity, BaseViewHolder> {

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public HomeNewsRecyclerAdapter() {
        addItemType(TYPE_ONE, R.layout.item_home_news_1);
        addItemType(TYPE_TWO, R.layout.item_home_news_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BaseHomeNewsEntity data) {
        if (holder.getItemViewType() == TYPE_ONE) {
            // holder.setImage(news_image,data.getUrl())
            holder.setText(R.id.news_title, data.getTitle());
        } else {
            holder.setText(R.id.news_title, data.getTitle());
        }

        View item = holder.itemView;
        ClickUtils.applyPressedViewScale(item);
        item.setOnClickListener(v -> {
            GoodsActivity.start(getContext(), new Bundle(), null);
        });
    }
}
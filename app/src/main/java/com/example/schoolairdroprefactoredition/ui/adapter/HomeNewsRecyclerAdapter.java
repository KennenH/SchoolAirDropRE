package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

import org.jetbrains.annotations.NotNull;

public class HomeNewsRecyclerAdapter extends BaseMultiItemQuickAdapter<BaseHomeNewsEntity, BaseViewHolder> implements View.OnClickListener {

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public HomeNewsRecyclerAdapter() {
        addItemType(TYPE_ONE, R.layout.item_home_news_1);
        addItemType(TYPE_TWO, R.layout.item_home_news_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BaseHomeNewsEntity item) {
        if (holder.getItemViewType() == TYPE_ONE) {
            // holder.setImage(news_image,item.getUrl())
            holder.setText(R.id.news_title, item.getTitle());
        } else {
            holder.setText(R.id.news_title, item.getTitle());
        }
    }


    @Override
    public void onClick(View v) {
        GoodsActivity.start(getContext(), null);
    }
}
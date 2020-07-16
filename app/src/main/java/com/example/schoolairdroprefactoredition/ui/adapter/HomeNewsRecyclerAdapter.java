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
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import org.jetbrains.annotations.NotNull;

/**
 * 新闻列表的adapter
 * todo 将BaseQuickAdapter内的泛型改为新闻bean类
 */
public class HomeNewsRecyclerAdapter extends BaseQuickAdapter<TestNewsItemBean, BaseViewHolder> implements View.OnClickListener {
    public HomeNewsRecyclerAdapter() {
        super(R.layout.item_home_news);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestNewsItemBean item) {
        if (item != null) {
            Glide.with(getContext())
                    .load(item.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(getContext().getResources().getDrawable(R.drawable.logo_120x, getContext().getTheme()))
                    .dontTransform()
                    .into((ImageView) holder.itemView.findViewById(R.id.news_cover));

            holder.setText(R.id.news_title, item.getTitle());
            holder.setText(R.id.news_day, item.getDay());
            holder.setText(R.id.news_month, item.getMonth());
            holder.setText(R.id.news_sender, item.getSender());

            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GoodsActivity.class);
        getContext().startActivity(intent);
    }
}
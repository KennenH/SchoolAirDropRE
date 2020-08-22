package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.quote.QuoteDetailActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class QuoteRecyclerAdapter extends BaseQuickAdapter<TestQuoteSectionItemBean, BaseViewHolder> {

    public QuoteRecyclerAdapter() {
        super(R.layout.item_quote);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestQuoteSectionItemBean bean) {


        holder.itemView.findViewById(R.id.user_avatar).setOnClickListener(v -> {
            // todo 以用户唯一标识号为参数开启用户个人信息页面
        });
        holder.itemView.findViewById(R.id.user_name).setOnClickListener(v -> {
            // todo 以用户唯一标识号为参数开启用户个人信息页面
        });
        holder.itemView.findViewById(R.id.detail).setOnClickListener(v -> {
            // todo 以报价单唯一标识号为参数开启报价详情页面
            QuoteDetailActivity.start(getContext());
        });
    }
}

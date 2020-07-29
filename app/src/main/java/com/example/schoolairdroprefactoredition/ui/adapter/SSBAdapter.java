package com.example.schoolairdroprefactoredition.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPrice;

import org.jetbrains.annotations.NotNull;

public class SSBAdapter extends BaseQuickAdapter<TestSSBItemBean, BaseViewHolder> {
    public SSBAdapter() {
        super(R.layout.item_ssb);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestSSBItemBean bean) {
        holder.itemView.setOnClickListener(v -> {
            // open goods detail page
            GoodsActivity.start(getContext());
        });

        holder.itemView.findViewById(R.id.ssb_item_more_action).setOnClickListener(v -> {
            // pop up more action window
        });


    }
}

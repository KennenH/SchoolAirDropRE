package com.example.schoolairdroprefactoredition.ui.components;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;

import org.jetbrains.annotations.NotNull;

public class HeaderOnlyRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HeaderOnlyRecyclerAdapter() {
        super(R.layout.item_home_goods_info);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
    }
}

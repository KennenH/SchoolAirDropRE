package com.example.schoolairdroprefactoredition.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.adapterbean.MorePanelBean;

import org.jetbrains.annotations.NotNull;

public class MorePanelAdapter extends BaseQuickAdapter<MorePanelBean, BaseViewHolder> {
    public MorePanelAdapter() {
        super(R.layout.item_more_panel);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MorePanelBean item) {
        holder.setImageResource(R.id.icon, item.getIcon());
    }
}

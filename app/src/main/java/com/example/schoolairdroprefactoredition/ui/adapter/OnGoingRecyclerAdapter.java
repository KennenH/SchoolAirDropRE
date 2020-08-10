package com.example.schoolairdroprefactoredition.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;

import org.jetbrains.annotations.NotNull;

public class OnGoingRecyclerAdapter extends BaseQuickAdapter<TestOnGoingBean, BaseViewHolder> {
    public OnGoingRecyclerAdapter() {
        super(R.layout.item_on_going);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestOnGoingBean testOnGoingBean) {

    }
}

package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;

import org.jetbrains.annotations.NotNull;

public class OnGoingRecyclerAdapter extends BaseQuickAdapter<TestOnGoingBean, BaseViewHolder> {
    public OnGoingRecyclerAdapter() {
        super(R.layout.item_on_going);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestOnGoingBean testOnGoingBean) {
        TextView contact = holder.itemView.findViewById(R.id.on_going_contact);
        contact.setOnClickListener(v -> ChatActivity.start(getContext()));
    }
}

package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.arrangeplace.SelectPositionActivity;
import com.example.schoolairdroprefactoredition.activity.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;

import org.jetbrains.annotations.NotNull;

public class UnselectedTransactionRecyclerAdapter extends BaseQuickAdapter<DomainUnselectedTransaction, BaseViewHolder> implements View.OnClickListener {
    public UnselectedTransactionRecyclerAdapter() {
        super(R.layout.item_position_to_do);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainUnselectedTransaction bean) {
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SelectPositionActivity.startForResult(getContext(), ChatActivity.SELECT_POSITION);
    }
}

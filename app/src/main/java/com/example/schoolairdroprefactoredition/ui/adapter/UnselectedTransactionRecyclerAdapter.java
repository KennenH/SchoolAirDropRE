package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;
import com.example.schoolairdroprefactoredition.scene.arrangeplace.ArrangePositionActivity;

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
        ArrangePositionActivity.startForResult(getContext());
    }
}

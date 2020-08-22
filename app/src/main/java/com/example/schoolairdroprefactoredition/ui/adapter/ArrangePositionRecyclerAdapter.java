package com.example.schoolairdroprefactoredition.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainArrangePosition;

import org.jetbrains.annotations.NotNull;

public class ArrangePositionRecyclerAdapter extends BaseQuickAdapter<DomainArrangePosition, BaseViewHolder> {
    public ArrangePositionRecyclerAdapter() {
        super(R.layout.item_position_arragne);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainArrangePosition item) {
        holder.setText(R.id.position, "中国计量大学西校区生活区门口");
        TextView position = holder.itemView.findViewById(R.id.position);
        position.setCompoundDrawables(getContext().getDrawable(R.drawable.ic_favorite_small), null, null, null);
    }
}

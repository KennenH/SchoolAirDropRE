package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.arrangeplace.ArrangePositionActivity;

import org.jetbrains.annotations.NotNull;

public class ArrangePositionRecyclerAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {
    public ArrangePositionRecyclerAdapter() {
        super(R.layout.item_position_arragne);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, PoiItem item) {
        holder.setText(R.id.position, item.getAdName());
        TextView position = holder.itemView.findViewById(R.id.position);
        position.setCompoundDrawables(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_small), null, null, null);

        holder.itemView.setOnClickListener(v -> {
            if (getContext() instanceof AppCompatActivity) {
                Intent intent = new Intent();
                intent.putExtra(ArrangePositionActivity.SELECT_POSITION_KEY, item.getAdName());
                ((AppCompatActivity) getContext()).setResult(ArrangePositionActivity.SELECT_POSITION, intent);
                ((AppCompatActivity) getContext()).finish();
            }
        });
    }
}

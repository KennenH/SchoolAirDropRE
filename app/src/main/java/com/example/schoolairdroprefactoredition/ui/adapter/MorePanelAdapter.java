package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.arrangeplace.UnArrangePositionTransactionActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.model.adapterbean.MorePanelBean;
import com.example.schoolairdroprefactoredition.utils.MyUtil;

import org.jetbrains.annotations.NotNull;

public class MorePanelAdapter extends BaseQuickAdapter<MorePanelBean, BaseViewHolder> {

    public MorePanelAdapter() {
        super(R.layout.item_more_panel);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MorePanelBean bean) {
        holder.setImageResource(R.id.icon, bean.getIcon());

        holder.itemView.setOnClickListener(v -> {
            switch (bean.getItem()) {
                case MorePanelBean.ALBUM:
                    if (getContext() instanceof AppCompatActivity)
                        MyUtil.pickPhotoFromAlbum((AppCompatActivity) getContext(), ChatActivity.TAKE_PHOTO, null, 6, false, false);
                    break;
                case MorePanelBean.CAMERA:
                    if (getContext() instanceof AppCompatActivity)
                        MyUtil.takePhoto((AppCompatActivity) getContext(), ChatActivity.PICK_PHOTO, false);
                    break;
                case MorePanelBean.QUOTE:

                    break;
                case MorePanelBean.POSITION:
                    UnArrangePositionTransactionActivity.start(getContext());
                    break;

            }
        });
    }
}

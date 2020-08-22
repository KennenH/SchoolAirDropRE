package com.example.schoolairdroprefactoredition.activity.chat.binder;

import com.chad.library.adapter.base.binder.QuickItemBinder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatReceiveImageEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

public class ChatReceiveImageBinder extends QuickItemBinder<ChatReceiveImageEntity> {
    @Override
    public int getLayoutId() {
        return R.layout.item_chat_receive_image;
    }

    @Override
    public void convert(@NotNull BaseViewHolder holder, ChatReceiveImageEntity data) {
        SimpleDraweeView image = holder.itemView.findViewById(R.id.receive_image);
        image.setImageURI(data.getImageUri());
    }
}

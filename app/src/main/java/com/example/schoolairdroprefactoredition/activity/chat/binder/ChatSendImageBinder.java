package com.example.schoolairdroprefactoredition.activity.chat.binder;

import com.chad.library.adapter.base.binder.QuickItemBinder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatSendImageEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

public class ChatSendImageBinder extends QuickItemBinder<ChatSendImageEntity> {
    @Override
    public int getLayoutId() {
        return R.layout.item_chat_send_image;
    }

    @Override
    public void convert(@NotNull BaseViewHolder holder, ChatSendImageEntity data) {
        SimpleDraweeView image = holder.itemView.findViewById(R.id.send_image);
        image.setImageURI(data.getImageUri());
    }
}

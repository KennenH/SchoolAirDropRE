package com.example.schoolairdroprefactoredition.activity.chat.binder;

import com.chad.library.adapter.base.binder.QuickItemBinder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatSendMessageEntity;

import org.jetbrains.annotations.NotNull;

public class ChatSendMessageBinder extends QuickItemBinder<ChatSendMessageEntity> {
    @Override
    public int getLayoutId() {
        return R.layout.item_chat_send;
    }

    @Override
    public void convert(@NotNull BaseViewHolder holder, ChatSendMessageEntity data) {
        holder.setText(R.id.send_image, data.getMessage());
    }


}

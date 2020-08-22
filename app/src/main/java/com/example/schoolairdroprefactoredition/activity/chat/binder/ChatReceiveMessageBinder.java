package com.example.schoolairdroprefactoredition.activity.chat.binder;

import com.chad.library.adapter.base.binder.QuickItemBinder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatReceiveMessageEntity;

import org.jetbrains.annotations.NotNull;

public class ChatReceiveMessageBinder extends QuickItemBinder<ChatReceiveMessageEntity> {
    @Override
    public int getLayoutId() {
        return R.layout.item_chat_receive;
    }

    @Override
    public void convert(@NotNull BaseViewHolder holder, ChatReceiveMessageEntity data) {
        holder.setText(R.id.receive_image, data.getMessage());
    }
}

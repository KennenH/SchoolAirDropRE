package com.example.schoolairdroprefactoredition.ui.adapter;

import com.chad.library.adapter.base.BaseBinderAdapter;
import com.example.schoolairdroprefactoredition.activity.chat.binder.ChatReceiveImageBinder;
import com.example.schoolairdroprefactoredition.activity.chat.binder.ChatReceiveMessageBinder;
import com.example.schoolairdroprefactoredition.activity.chat.binder.ChatSendImageBinder;
import com.example.schoolairdroprefactoredition.activity.chat.binder.ChatSendMessageBinder;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatReceiveImageEntity;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatReceiveMessageEntity;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatSendImageEntity;
import com.example.schoolairdroprefactoredition.activity.chat.entity.ChatSendMessageEntity;

public class ChatRecyclerAdapter extends BaseBinderAdapter {

    public ChatRecyclerAdapter() {
        super();
        init();
    }

    private void init() {
        addItemBinder(ChatSendMessageEntity.class, new ChatSendMessageBinder())
                .addItemBinder(ChatReceiveMessageEntity.class, new ChatReceiveMessageBinder())
                .addItemBinder(ChatSendImageEntity.class, new ChatSendImageBinder())
                .addItemBinder(ChatReceiveImageEntity.class, new ChatReceiveImageBinder());
    }
}

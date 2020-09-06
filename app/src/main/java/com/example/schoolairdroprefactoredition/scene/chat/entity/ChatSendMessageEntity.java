package com.example.schoolairdroprefactoredition.scene.chat.entity;

public class ChatSendMessageEntity {

    private String message;

    public ChatSendMessageEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

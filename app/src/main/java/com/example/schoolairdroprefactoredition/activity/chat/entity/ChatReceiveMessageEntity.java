package com.example.schoolairdroprefactoredition.activity.chat.entity;

public class ChatReceiveMessageEntity {
    private String message;

    public ChatReceiveMessageEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

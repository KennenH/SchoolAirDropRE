package com.example.schoolairdroprefactoredition.scene.chat.entity;

public class ChatSendImageEntity {
    private String imageUri;

    public ChatSendImageEntity(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

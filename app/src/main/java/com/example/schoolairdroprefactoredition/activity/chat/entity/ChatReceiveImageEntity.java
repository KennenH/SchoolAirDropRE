package com.example.schoolairdroprefactoredition.activity.chat.entity;

public class ChatReceiveImageEntity {
    private String imageUri;

    public ChatReceiveImageEntity(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

package com.example.schoolairdroprefactoredition.cache;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class AddNewDraftCache {
    public static final String ADD_NEW_DRAFT = "AddNewDraft";

    private String cover;
    private List<LocalMedia> picSet;
    private String title;
    private String price;
    private String description;
    private boolean isNegotiable;
    private boolean isSecondHand;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<LocalMedia> getPicSet() {
        return picSet;
    }

    public void setPicSet(List<LocalMedia> picSet) {
        this.picSet = picSet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNegotiable() {
        return isNegotiable;
    }

    public void setNegotiable(boolean negotiable) {
        isNegotiable = negotiable;
    }

    public boolean isSecondHand() {
        return isSecondHand;
    }

    public void setSecondHand(boolean secondHand) {
        isSecondHand = secondHand;
    }
}

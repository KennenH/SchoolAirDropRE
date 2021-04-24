package com.example.schoolairdroprefactoredition.database;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter;

import java.io.Serializable;

public class BaseIWantEntity implements MultiItemEntity, Serializable {

    private int type = IWantRecyclerAdapter.TYPE_ONE;
    private String content;
    private String images;
    private int userID;
    private String tag;
    private int color = IWantRecyclerAdapter.COLOR_DEFAULT;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return type;
    }
}

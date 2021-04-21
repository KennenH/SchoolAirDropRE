package com.example.schoolairdroprefactoredition.ui.components;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter;

import java.io.Serializable;

public class BaseIWantEntity implements MultiItemEntity, Serializable {

    private int type = IWantRecyclerAdapter.TYPE_ONE;
    private String content;
    private int color = IWantRecyclerAdapter.COLOR_GREY;

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

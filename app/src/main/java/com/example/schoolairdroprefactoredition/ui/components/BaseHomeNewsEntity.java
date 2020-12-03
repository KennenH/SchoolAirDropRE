package com.example.schoolairdroprefactoredition.ui.components;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;

public class BaseHomeNewsEntity implements MultiItemEntity {

    private int type = HomeNewsRecyclerAdapter.TYPE_ONE;
    private String title;
    private String uri;

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return uri;
    }

    public void setUrl(String uri) {
        this.uri = uri;
    }

    @Override
    public int getItemType() {
        return type;
    }
}

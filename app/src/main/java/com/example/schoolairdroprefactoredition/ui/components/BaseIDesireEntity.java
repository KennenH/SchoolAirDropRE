package com.example.schoolairdroprefactoredition.ui.components;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.schoolairdroprefactoredition.ui.adapter.IDesireRecyclerAdapter;

public class BaseIDesireEntity implements MultiItemEntity {

    private int type = IDesireRecyclerAdapter.TYPE_ONE;
    private String content;

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

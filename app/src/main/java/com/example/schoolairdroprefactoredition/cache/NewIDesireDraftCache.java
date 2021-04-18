package com.example.schoolairdroprefactoredition.cache;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 新求购的草稿
 */
public class NewIDesireDraftCache {
    public static final String KEY = "imINQUIRYdraftAndU?";

    /**
     * 求购描述
     */
    private String content;

    /**
     * 求购标签
     */
    private String tag;

    /**
     * 求购图片集
     */
    private List<LocalMedia> picSet;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<LocalMedia> getPicSet() {
        return picSet;
    }

    public void setPicSet(List<LocalMedia> picSet) {
        this.picSet = picSet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

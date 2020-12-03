package com.example.schoolairdroprefactoredition.cache;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 新帖子的草稿
 */
public class NewPostDraftCache {
    public static final String NEW_POST_DRAFT = "imPOSTdraftAndU?";

    private String cover;
    private float hwRatio;
    private List<LocalMedia> picSet;
    private String title;
    private String content;

    public float getHwRatio() {
        return hwRatio;
    }

    public void setHwRatio(float hwRatio) {
        this.hwRatio = hwRatio;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

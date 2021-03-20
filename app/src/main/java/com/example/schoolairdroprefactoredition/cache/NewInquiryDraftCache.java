package com.example.schoolairdroprefactoredition.cache;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 新求购的草稿
 */
public class NewInquiryDraftCache {
    public static final String KEY = "imINQUIRYdraftAndU?";

    private String tag;
    private boolean isAnonymous;
    private String cover;
    private float hwRatio;
    private List<LocalMedia> picSet;
    private String title;
    private String content;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

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

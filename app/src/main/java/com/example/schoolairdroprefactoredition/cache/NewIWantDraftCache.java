package com.example.schoolairdroprefactoredition.cache;

import com.example.schoolairdroprefactoredition.domain.DomainIWantTags;
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 新求购的草稿
 */
public class NewIWantDraftCache {
    public static final String KEY = "imINQUIRYdraftAndU?";

    /**
     * 求购描述
     */
    private String content;

    /**
     * 求购标签
     */
    private DomainIWantTags.Data tag;

    /**
     * 求购卡片颜色
     *
     * {@link IWantRecyclerAdapter#COLOR_DEFAULT} 默认
     * {@link IWantRecyclerAdapter#COLOR_PURPLE} 紫色
     * {@link IWantRecyclerAdapter#COLOR_THEME} 主题色
     * {@link IWantRecyclerAdapter#COLOR_HEART} 红色
     * {@link IWantRecyclerAdapter#COLOR_WARNING} 橘色
     */
    private int cardColor;

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    /**
     * 求购图片集
     */
    private List<LocalMedia> picSet;

    public DomainIWantTags.Data getTag() {
        return tag;
    }

    public void setTag(DomainIWantTags.Data tag) {
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

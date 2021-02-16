package com.example.schoolairdroprefactoredition.scene.chat.panel;

import androidx.annotation.DrawableRes;

public class MorePanelBean {

    public static final int CAMERA = 200; // 相机 按钮唯一标识码
    public static final int ALBUM = 300; // 照片 按钮唯一标识码

    public MorePanelBean(int icon, int itemID) {
        this.icon = icon;
        this.itemID = itemID;
    }

    @DrawableRes
    private int icon;

    private int itemID;

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}

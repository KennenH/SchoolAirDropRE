package com.example.schoolairdroprefactoredition.pojo.adapterbean;

import androidx.annotation.DrawableRes;

public class MorePanelBean {

    public static final int CAMERA = 200; // item 相机
    public static final int ALBUM = 300; // item 照片

    public MorePanelBean(int icon, int item) {
        this.icon = icon;
        this.item = item;
    }

    @DrawableRes
    private int icon;

    private int item;

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }
}

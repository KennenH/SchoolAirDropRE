package com.example.schoolairdroprefactoredition.model.adapterbean;

import androidx.annotation.DrawableRes;

public class MorePanelBean {

    public MorePanelBean(int icon) {
        this.icon = icon;
    }

    @DrawableRes
    private int icon;

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }
}

package com.example.schoolairdroprefactoredition.domain;

import android.graphics.drawable.Drawable;

public class SearchSuggestionBean {
    private String title;
    private Drawable drawable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}

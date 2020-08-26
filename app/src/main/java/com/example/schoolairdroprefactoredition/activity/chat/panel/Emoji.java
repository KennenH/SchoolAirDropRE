package com.example.schoolairdroprefactoredition.activity.chat.panel;

import androidx.annotation.DrawableRes;

public class Emoji {
    public String text;

    @DrawableRes
    public int drawableRes;

    public Emoji(String text, @DrawableRes int drawableRes) {
        this.text = text;
        this.drawableRes = drawableRes;
    }
}

package com.example.schoolairdroprefactoredition.utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorUtil {
    /**
     * 判断该颜色是否为暗色
     *
     * @param color 颜色
     * @return 是否为暗色
     */
    public static boolean isColorDark(@ColorInt int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return !(darkness < 0.5);
    }
}

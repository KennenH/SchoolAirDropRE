package com.example.schoolairdroprefactoredition.utils.decoration;

import com.blankj.utilcode.util.ScreenUtils;

public class DecorationUtil {

    /**
     * 均匀分布的格子布局
     *
     * @param span      一行item的个数
     * @param itemWidth 每个item的宽度, in dp
     */
    public static int getSpace(int span, float itemWidth) {
        float rowSpace = ((ScreenUtils.getAppScreenWidth() - span * itemWidth) / ((span + 1) * 2f));
        return (int) (rowSpace < 1f ? 0 : rowSpace - 0.5f);
    }
}

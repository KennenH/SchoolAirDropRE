package com.example.schoolairdroprefactoredition.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ScreenUtils;

public class MyUtil {
    /**
     * 屏幕横向平均分配item时需要的margin
     *
     * @param items 一行item数量
     * @param size  item的宽
     * @return margin
     */
    public static int averageItemMargin(int items, int size) {
        return (int) ((ScreenUtils.getAppScreenWidth() - items * size) / (2f * items + 2));
    }

    /**
     * 分页每页固定数量item获取本页数组上限
     *
     * @param total       总共item数量
     * @param itemPerPage 每页item数
     * @param position    当前页
     * @return 当前数组上限（exclusive）
     */
    public static int gridItemBounds(int total, int itemPerPage, int position) {
        return Math.min((position + 1) * itemPerPage, total);
    }
}

package com.example.schoolairdroprefactoredition.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.schoolairdroprefactoredition.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

    public static void setupFullHeight(Context context, BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight(context);
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private static int getWindowHeight(Context context) {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}

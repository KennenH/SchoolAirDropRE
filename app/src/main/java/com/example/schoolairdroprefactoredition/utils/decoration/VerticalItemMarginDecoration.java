package com.example.schoolairdroprefactoredition.utils.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * 仅垂直方向decoration
 * /代表decoration的区域
 * ////////////////////
 * ———————————————————
 * | <-Match Parent-> |
 * ———————————————————
 * ///////////////////
 * 四周型 {@link MarginItemDecoration}
 * 仅水平方向 {@link HorizontalItemMarginDecoration}
 */
public class VerticalItemMarginDecoration extends RecyclerView.ItemDecoration {
    private int space = ScreenUtils.getAppScreenWidth() / 140;

    public VerticalItemMarginDecoration() {
    }

    public VerticalItemMarginDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            parent.setPadding(0, space, 0, space);
            parent.setClipToPadding(false);
        }

        outRect.top = space;
        outRect.bottom = space;
    }

}
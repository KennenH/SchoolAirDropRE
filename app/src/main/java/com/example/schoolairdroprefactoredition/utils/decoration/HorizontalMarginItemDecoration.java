package com.example.schoolairdroprefactoredition.utils.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * 仅水平方向decoration
 * +代表decoration
 * ++  ——————————————————— ++
 * ++ |       View       | ++
 * ++ ———————————————————  ++
 *
 * 仅垂直方向 {@link VerticalMarginItemDecoration}
 * 四周型 {@link MarginItemDecoration}
 */
public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {
    private int space = ScreenUtils.getAppScreenWidth() / 70;

    public HorizontalMarginItemDecoration(){}

    public HorizontalMarginItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            parent.setPadding(space, 0, space, 0);
            parent.setClipToPadding(false);
        }

        outRect.left = space;
        outRect.right = space;
    }

}
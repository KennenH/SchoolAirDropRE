package com.example.schoolairdroprefactoredition.utils.decoration;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.schoolairdroprefactoredition.R;

/**
 * 四周型decoration
 * +代表decoration
 * ++++++++++++++++++++++++++
 * ++  ——————————————————— ++
 * ++ |       View        |++
 * ++ ———————————————————  ++
 * ++++++++++++++++++++++++++
 * <p>
 * 仅垂直方向 {@link VerticalMarginItemDecoration}
 * 仅水平方向 {@link HorizontalMarginItemDecoration}
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int space = ScreenUtils.getAppScreenWidth() / 90;

    public MarginItemDecoration() {
    }

    public MarginItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            parent.setPadding(space, space, space, space);
            parent.setClipToPadding(false);
        }
        outRect.bottom = space;
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
    }

}
package com.example.schoolairdroprefactoredition.utils.decoration;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * 四周型decoration
 * +代表decoration
 * ++++++++++++++++++++++++++
 * ++  ——————————————————— ++
 * ++ |       View        |++
 * ++ ———————————————————  ++
 * ++++++++++++++++++++++++++
 * <p>
 * 仅垂直方向 {@link VerticalItemMarginDecoration}
 * 仅水平方向 {@link HorizontalItemMarginDecoration}
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int space = ScreenUtils.getAppScreenWidth() / 85;

    public MarginItemDecoration() {
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        if (parent.getPaddingLeft() != space || parent.getPaddingRight() != space) {
//        }
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
        parent.setPadding(space, 0, space, space);
    }

}
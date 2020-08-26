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
    private int space = ScreenUtils.getAppScreenWidth() / 70;
    private boolean noTop = false;

    public MarginItemDecoration() {
    }

    public MarginItemDecoration(boolean noTop) {
        this.noTop = noTop;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space)
            parent.setPadding(space, 0, space, 0);

        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0 && !noTop)
            outRect.top = space * 2;
        else if (pos != 0)
            outRect.top = space;

        if (pos == parent.getChildCount())
            outRect.bottom = space * 2;
        else
            outRect.bottom = space;

        outRect.right = space;
        outRect.left = space;
    }

}
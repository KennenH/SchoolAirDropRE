package com.example.schoolairdroprefactoredition.utils.decoration;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

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
    private int space = SizeUtils.dp2px(8f);
    private boolean isHome = false;

    public MarginItemDecoration() {

    }

    public MarginItemDecoration(int space, boolean isHome) {
        this.space = space;
        this.isHome = isHome;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            if (!isHome)
                parent.setPadding(space, 0, space, space);
        }

        outRect.top = parent.getChildAdapterPosition(view) == 0 ? 2 * space : space;
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
    }

}
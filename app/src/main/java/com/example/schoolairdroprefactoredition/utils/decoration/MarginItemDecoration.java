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
    private int space = SizeUtils.dp2px(3f);
    private int spaceB = SizeUtils.dp2px(5f);

    public MarginItemDecoration() {

    }

    public MarginItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space)
            parent.setPadding(spaceB, spaceB, spaceB, spaceB);

        outRect.top = space;
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
    }

}
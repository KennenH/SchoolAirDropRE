package com.example.schoolairdroprefactoredition.utils.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public GridItemDecoration(int span, float itemWidth) {
        if (span < 1)
            throw new IllegalArgumentException("span cannot smaller than 1");

        space = (int) ((ScreenUtils.getAppScreenWidth() - span * itemWidth) / ((span + 1) * 2f));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            parent.setPadding(space, space / 2, space, space / 2);
        }
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;

    }
}

package com.example.schoolairdroprefactoredition.utils.decoration;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    /**
     * 均匀分布的格子布局
     *
     * @param span      一行item的个数
     * @param itemWidth 每个item的宽度, in dp
     */
    public GridItemDecoration(int span, float itemWidth) {
        if (span < 1)
            throw new IllegalArgumentException("span cannot less than 1");

        this.space = DecorationUtil.getSpace(span, itemWidth);
        Log.d("哈哈哈哈哈啊哈哈", "span -- > " + span + " itemWidth -- > " + itemWidth + " screenWidth -- > " + ScreenUtils.getAppScreenWidth() + " space -- > " + space
        );
    }

    public GridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getPaddingLeft() != space) {
            parent.setPadding(space, space, space, space);
        }
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}

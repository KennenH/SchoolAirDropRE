package com.example.schoolairdroprefactoredition.utils.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

public class VerticalItemLineDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private static final float WIDTH_PERCENT = 0.8f;
    private final float lineWidth = SizeUtils.dp2px(0.8f);

    public VerticalItemLineDecoration(@ColorInt @IdRes int decorationColor) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(SizeUtils.dp2px(1));
        mPaint.setColor(decorationColor);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = (int) lineWidth;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 1; i < count; i++) {
            final float startY = parent.getChildAt(i).getTop() - lineWidth;
            c.drawRect(parent.getLeft() + ScreenUtils.getScreenWidth() * (1f - WIDTH_PERCENT), startY, parent.getRight(), startY + lineWidth, mPaint);
        }
    }
}

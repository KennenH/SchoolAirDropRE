package com.example.schoolairdroprefactoredition.utils.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

/**
 * item 间分割线以及两个header
 */
public class QuoteDecoration extends RecyclerView.ItemDecoration {
    private Paint mHeaderPaint;
    private Paint mDecorationPaint;
    private Paint mTextPaint;

    private int unhandled;
    // flag whether unprocessed header has been drawn
    private boolean unprocessedHasDrawn = false;

    private static final float WIDTH_PERCENT = 0.75f;
    private float headerHeight;
    private final float fontSize = SizeUtils.dp2px(14);
    private final float gap = SizeUtils.dp2px(8);

    private String unprocessed;
    private String processed;

    public QuoteDecoration(Context context, int total, int unhandled) {
        this.unhandled = unhandled;
        final int handled = total - unhandled;

        mHeaderPaint = new Paint();
        mDecorationPaint = new Paint();
        mTextPaint = new Paint();

        unprocessed = context.getResources().getString(R.string.unprocessedSub, unhandled);
        processed = context.getResources().getString(R.string.processedSub, handled);
        headerHeight = context.getResources().getDimension(R.dimen.icon_normal);
        mHeaderPaint.setColor(context.getColor(R.color.primary));
        mDecorationPaint.setStrokeWidth(gap);
        mDecorationPaint.setColor(context.getColor(R.color.white));

        mTextPaint.setColor(context.getColor(R.color.primaryText));
        mTextPaint.setTextSize(fontSize);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (outRect.top == 0) {
            final int pos = parent.getChildAdapterPosition(view);
            if (pos == 0 || pos == unhandled) {
                if (unhandled != 0)
                    outRect.top = (int) headerHeight;
                else
                    outRect.top = (int) headerHeight * 2;
            } else {
                outRect.top = (int) gap * 2;
            }

            if (parent.getAdapter() != null &&
                    pos == parent.getAdapter().getItemCount() - 1)
                outRect.bottom = (int) gap * 2;

            outRect.right = (int) gap * 2;
            outRect.left = (int) gap * 2;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            final int pos = parent.getChildAdapterPosition(child);
            float top = child.getTop();
            if (pos == 0 || pos == unhandled) {
                if (unhandled == 0) { // unhandled == 0 时，未处理和已处理头部分开绘制
                    c.drawRect(parent.getLeft(), top - 2f * headerHeight, parent.getRight(), top - headerHeight, mHeaderPaint);
                    c.drawText(unprocessed, parent.getLeft() + gap * 2f, top - headerHeight * 3f / 2f + fontSize / 2f, mTextPaint);
                    c.drawRect(parent.getLeft(), top - headerHeight, parent.getRight(), top, mHeaderPaint);
                    c.drawText(processed, parent.getLeft() + gap * 2f, top - headerHeight / 2f + fontSize / 2f, mTextPaint);
                } else {
                    String curText = pos == 0 ? unprocessed : processed;
                    c.drawRect(parent.getLeft(), top - headerHeight, parent.getRight(), top, mHeaderPaint);
                    c.drawText(curText, parent.getLeft() + gap * 2f, top - headerHeight / 2f + fontSize / 2f, mTextPaint);
                }
            }
        }
    }
}

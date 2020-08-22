package com.example.schoolairdroprefactoredition.utils.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
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

    private static final float WIDTH_PERCENT = 0.75f;
    private float headerHeight;
    private float padding;
    private final float fontSize = SizeUtils.sp2px(12);
    private final float gap = SizeUtils.dp2px(8f);

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
        headerHeight = context.getResources().getDimension(R.dimen.toolbar_icon_bit_larger);
        padding = context.getResources().getDimension(R.dimen.general_padding);
        mHeaderPaint.setColor(context.getResources().getColor(R.color.primary));
        mDecorationPaint.setStrokeWidth(gap);
        mDecorationPaint.setColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mTextPaint.setColor(context.getResources().getColor(R.color.primaryText, context.getTheme()));
        else
            mTextPaint.setColor(context.getResources().getColor(R.color.primaryText));
        mTextPaint.setTextSize(fontSize);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int pos = parent.getChildAdapterPosition(view);
        if (pos == 0 || pos == unhandled)
            outRect.top = (int) headerHeight;
        else
            outRect.top = (int) gap;

        outRect.right = (int) gap;
        outRect.left = (int) gap;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            final int pos = parent.getChildAdapterPosition(child);
//            final float startY = parent.getChildAt(i).getTop() - lineWidth;
            if (pos == 0 || pos == unhandled) {
                float top = child.getTop();
                String curText = pos == 0 ? unprocessed : processed;
                c.drawRect(parent.getLeft(), top - headerHeight, parent.getRight(), top, mHeaderPaint);
                c.drawText(curText, parent.getLeft() + padding, top - headerHeight / 2f + fontSize / 2f, mTextPaint);
            }
        }
    }
}

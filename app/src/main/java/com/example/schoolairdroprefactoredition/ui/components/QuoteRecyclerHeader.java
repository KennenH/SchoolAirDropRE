package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class QuoteRecyclerHeader extends ConstraintLayout {
    public static final int UNPROCESSED = R.string.unprocessed;
    public static final int PROCESSED = R.string.processed;

    private TextView mTitle;
    private TextView mNum;

    @IntDef({UNPROCESSED, PROCESSED})
    public @interface QuoteTitleType {
    }

    public QuoteRecyclerHeader(Context context) {
        this(context, null);
    }

    public QuoteRecyclerHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteRecyclerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_quote_section_recycler_header, this, true);

        mTitle = findViewById(R.id.title);
        mNum = findViewById(R.id.num);
    }

    public void setTitle(@QuoteTitleType int type) {
        mTitle.setText(type);
    }

    public void setNum(int num) {
        mNum.setText(getContext().getString(R.string.quoteHeaderNum, num));
    }
}

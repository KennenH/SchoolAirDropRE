package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import com.example.schoolairdroprefactoredition.R;

public class RecyclerFooter extends TextViewWithImages {

    public RecyclerFooter(Context context) {
        this(context, null);
    }

    public RecyclerFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int padding = (int) getContext().getResources().getDimension(R.dimen.general_padding);
        setPadding(padding, padding, padding, padding);
        setText(getContext().getResources().getString(R.string.recyclerFooter));
    }
}

package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

public class RecyclerFooter extends ConstraintLayoutAuto {

    public RecyclerFooter(Context context) {
        this(context, null);
    }

    public RecyclerFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.component_recycler_footer, this, true);
    }
}

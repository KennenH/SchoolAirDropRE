package com.example.schoolairdroprefactoredition.ui.auto;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import me.jessyan.autosize.AutoSizeCompat;

public class ConstraintLayoutAuto extends ConstraintLayout {
    public ConstraintLayoutAuto(Context context) {
        super(context);
    }

    public ConstraintLayoutAuto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintLayoutAuto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
        return super.generateLayoutParams(attrs);
    }
}

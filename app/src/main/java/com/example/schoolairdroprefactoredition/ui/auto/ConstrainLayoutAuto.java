package com.example.schoolairdroprefactoredition.ui.auto;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import me.jessyan.autosize.AutoSizeCompat;

public class ConstrainLayoutAuto extends ConstraintLayout {
    public ConstrainLayoutAuto(Context context) {
        super(context);
    }

    public ConstrainLayoutAuto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstrainLayoutAuto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
        return super.generateLayoutParams(attrs);
    }
}

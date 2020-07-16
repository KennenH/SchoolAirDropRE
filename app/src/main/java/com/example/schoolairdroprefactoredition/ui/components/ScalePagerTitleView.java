package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.Typeface;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class ScalePagerTitleView extends SimplePagerTitleView {

    private float mMinScale = 0.9f;

    public ScalePagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);
        setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
        setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
        if (leavePercent > 0.5f) {
            setTypeface(Typeface.create(getTypeface(), Typeface.NORMAL), Typeface.NORMAL);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);
        setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);
        if (enterPercent > 0.5f) {
            setTypeface(getTypeface(), Typeface.BOLD);
        }
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float mMinScale) {
        this.mMinScale = mMinScale;
    }
}

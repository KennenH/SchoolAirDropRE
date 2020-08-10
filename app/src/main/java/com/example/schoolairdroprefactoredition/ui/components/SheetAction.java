package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.SheetActionPagerAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SheetAction extends BottomSheetDialog {

    private ViewPager mPager;
    private LinearLayout mIndicator;
    private SheetActionPagerAdapter mPagerAdapter = new SheetActionPagerAdapter();

    public SheetAction(@NonNull Context context) {
        super(context);
        init();
    }

    public SheetAction(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    protected SheetAction(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.sheet_action);

        mPager = findViewById(R.id.sheet_action_pager);
        mIndicator = findViewById(R.id.sheet_action_indicator);

        mPager.setAdapter(mPagerAdapter);
        updateDotsIndicator();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateDotsIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateDotsIndicator();
            }
        });

        View view = getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (view != null) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.transparent, getContext().getTheme()));
            final BottomSheetBehavior behavior = BottomSheetBehavior.from(view);
            behavior.setSkipCollapsed(true);
            setCanceledOnTouchOutside(true);
        }
    }

    private void updateDotsIndicator() {
        if (mPagerAdapter != null) {
            //小白点实际数量
            int indicatorNum = mPagerAdapter.getCount();

            mIndicator.removeAllViews();
            for (int i = 0; i < indicatorNum; i++) {
                View dot = new View(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(6), SizeUtils.dp2px(6));
                params.setMargins(SizeUtils.dp2px(5), SizeUtils.dp2px(0), SizeUtils.dp2px(5), SizeUtils.dp2px(0));
                dot.setLayoutParams(params);
                if (mPager.getCurrentItem() == i) {
                    dot.setBackground(getContext().getResources().getDrawable(R.drawable.indicator_dark, getContext().getTheme()));
                } else {
                    dot.setBackground(getContext().getResources().getDrawable(R.drawable.indicator_light, getContext().getTheme()));
                }
                mIndicator.addView(dot);
            }
        }
    }
}

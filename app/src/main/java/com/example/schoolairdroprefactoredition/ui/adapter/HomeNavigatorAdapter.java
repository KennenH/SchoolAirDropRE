package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.ScalePagerTitleView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.Arrays;
import java.util.List;

public class HomeNavigatorAdapter extends CommonNavigatorAdapter {

    private List<String> mList;
    private ViewPager mViewPager;

    public HomeNavigatorAdapter(Context context, ViewPager viewPager) {
        mViewPager = viewPager;

        String[] menu = {context.getString(R.string.home), context.getString(R.string.nearby)};
        mList = Arrays.asList(menu);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        ScalePagerTitleView title = new ScalePagerTitleView(context);
        title.setNormalColor(Color.BLACK);
        title.setSelectedColor(Color.BLACK);
        title.setTextSize(SizeUtils.sp2px(12));
        title.setOnClickListener(v -> mViewPager.setCurrentItem(index));
        title.setText(mList.get(index));

        return title;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setLineHeight(SizeUtils.dp2px(4));
        indicator.setRoundRadius(SizeUtils.dp2px(4));
        indicator.setYOffset(SizeUtils.dp2px(8));
        indicator.setColors(Color.parseColor("#60A3F7"));

        return indicator;
    }
}

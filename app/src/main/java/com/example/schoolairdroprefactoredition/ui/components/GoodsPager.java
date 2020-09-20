package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsPagerBinding;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class GoodsPager extends ConstraintLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<String> mData = new ArrayList<>();

    private ComponentGoodsPagerBinding binding;
    private LinearLayout mIndicatorContainer;
    private TextView mCurrent;
    private TextView mTotal;

    public GoodsPager(Context context) {
        this(context, null);
    }

    public GoodsPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentGoodsPagerBinding.inflate(LayoutInflater.from(context), this, true);
        mIndicatorContainer = findViewById(R.id.goods_pager_indicator);
        mViewPager = findViewById(R.id.goods_pager_pager);
        mCurrent = findViewById(R.id.goods_pager_current);
        mTotal = findViewById(R.id.goods_pager_total);

        mViewPager.addOnPageChangeListener(this);
        GoodsPagerAdapter mAdapter = new GoodsPagerAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    public void setData(List<String> data) {
        if (data.size() < 2)
            mIndicatorContainer.setVisibility(GONE);
        else
            mIndicatorContainer.setVisibility(VISIBLE);

        mData = data;
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing
    }

    @Override
    public void onPageSelected(int position) {
        mCurrent.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing
    }

    public class GoodsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            int size = mData.size();
            mTotal.setText(getContext().getString(R.string.indicator, size));
            return size;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            SimpleDraweeView pic = new SimpleDraweeView(getContext());
            pic.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + mData.get(position));
//            Glide.with(getContext())
//                    .load(mData.get(position))
//                    .centerCrop()
//                    .placeholder(R.drawable.logo_placeholder)
//                    .into(pic);
            if (pic.getParent() instanceof ViewGroup)
                ((ViewGroup) pic.getParent()).removeView(pic);

            container.addView(pic);
            return pic;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}

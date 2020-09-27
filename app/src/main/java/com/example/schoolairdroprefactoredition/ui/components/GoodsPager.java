package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsPagerBinding;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

public class GoodsPager extends ConstraintLayout implements ViewPager.OnPageChangeListener {

    private List<Object> mData = new ArrayList<>();

    private ComponentGoodsPagerBinding binding;

    public GoodsPager(Context context) {
        this(context, null);
    }

    public GoodsPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentGoodsPagerBinding.inflate(LayoutInflater.from(context), this, true);

        GoodsPagerAdapter mAdapter = new GoodsPagerAdapter();
        binding.goodsPagerPager.addOnPageChangeListener(this);
        binding.goodsPagerPager.setAdapter(mAdapter);
    }

    public void setData(List<String> data) {
        if (data.size() < 2)
            binding.goodsPagerIndicator.setVisibility(GONE);
        else
            binding.goodsPagerIndicator.setVisibility(VISIBLE);

        for (String pic : data)
            mData.add(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + pic);

        if (binding.goodsPagerPager.getAdapter() != null)
            binding.goodsPagerPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing
    }

    @Override
    public void onPageSelected(int position) {
        binding.goodsPagerCurrent.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing
    }

    public class GoodsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            int size = mData.size();
            binding.goodsPagerTotal.setText(getContext().getString(R.string.indicator, size));
            return size;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final ImageView pic = new ImageView(getContext());
            Glide.with(pic)
                    .load((String) mData.get(position))
                    .apply(new RequestOptions().placeholder(R.drawable.logo_placeholder).override(Target.SIZE_ORIGINAL))
                    .into(pic);

            pic.setOnClickListener(v -> new XPopup.Builder(getContext())
                    .asImageViewer(pic, position, mData, false, false, -1, -1, -1, true, (popupView, position1) -> {
                        binding.goodsPagerPager.setCurrentItem(position1, false);
                        popupView.updateSrcView((ImageView) binding.goodsPagerPager.getChildAt(position1));
                    }, new MyUtil.ImageLoader())
                    .show());

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

package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentImagePagerBinding;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

public class ImagePager extends ConstraintLayout implements ViewPager.OnPageChangeListener {

    private final List<Object> mData = new ArrayList<>();

    private final ComponentImagePagerBinding binding;

    private long lastClickTime = 0;

    private OnFirstImageLoadedListener mOnFirstImageLoadedListener;

    private boolean isOtherResourceFitFirst = false; // 是否使第一张图片为封面并使之后的图片以它为基准去fit它的x与y

    public ImagePager(Context context) {
        this(context, null);
    }

    public ImagePager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentImagePagerBinding.inflate(LayoutInflater.from(context), this, true);

        ImagePagerAdapter mAdapter = new ImagePagerAdapter();
        binding.goodsPagerPager.addOnPageChangeListener(this);
        binding.goodsPagerPager.setAdapter(mAdapter);
    }

    /**
     * 设置其他图片fit第一张图片的bounds
     * <p>
     * {@link #isOtherResourceFitFirst}
     */
    public void setOtherResourceFitFirst() {
        isOtherResourceFitFirst = true;
    }

    public void setData(List<String> data, boolean isIncludeBaseUrl) {
        if (data.size() < 2) {
            binding.goodsPagerIndicator.setVisibility(GONE);
        } else {
            binding.goodsPagerIndicator.setVisibility(VISIBLE);
        }

        for (String pic : data) {
            if (isIncludeBaseUrl) {
                mData.add(pic);
            } else {
                mData.add(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + ImageUtil.fixUrl(pic));
            }
        }

        if (binding.goodsPagerPager.getAdapter() != null) {
            binding.goodsPagerPager.getAdapter().notifyDataSetChanged();
        }
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

    /**
     * 第一张图片加载完毕
     * 便于activity开启元素共享动画
     */
    public interface OnFirstImageLoadedListener {
        void onFirstImageLoaded(boolean success);
    }

    public void setOnFirstImageLoadedListener(OnFirstImageLoadedListener listener) {
        this.mOnFirstImageLoadedListener = listener;
    }

    public class ImagePagerAdapter extends PagerAdapter {
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

            if (isOtherResourceFitFirst && position == 0) {
                pic.setAdjustViewBounds(true);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if (position == 0) {
                container.setLayoutParams(new Constraints.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(280f)));
            }

            Glide.with(pic)
                    .asBitmap()
                    .load((String) mData.get(position))
                    .encodeQuality(ConstantUtil.ORIGIN)
                    .apply(new RequestOptions().placeholder(R.drawable.logo_placeholder))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            if (position == 0 && mOnFirstImageLoadedListener != null) {
                                mOnFirstImageLoadedListener.onFirstImageLoaded(false);
                            }

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (position == 0 && mOnFirstImageLoadedListener != null) {
//                                LogUtils.d(
//                                        "res width -- > " + resource.getWidth()
//                                                + "res height -- > " + resource.getHeight()
//                                                + "\nresized height -- > " + ImageUtil.getResizedHeight(resource.getWidth(), resource.getHeight(), SizeUtils.dp2px(400f))
//                                                + "\n 400dp -- > " + SizeUtils.dp2px(400f));

                                mOnFirstImageLoadedListener.onFirstImageLoaded(true);

                                if (isOtherResourceFitFirst) {
                                    container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ImageUtil
                                            .getResizedHeight(resource.getWidth(), resource.getHeight(), SizeUtils.dp2px(400f))));
                                }
                            }
                            return false;
                        }
                    })
                    .into(pic);

            pic.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < ConstantUtil.MENU_CLICK_GAP)
                    return;
                lastClickTime = SystemClock.elapsedRealtime();

                new XPopup.Builder(getContext())
                        .hasStatusBar(false)
                        .asImageViewer(pic, position, mData, false, false, -1, -1, -1, true, (popupView, position1) -> {
                            popupView.updateSrcView((ImageView) binding.goodsPagerPager.getChildAt(position1));
                        }, new MyUtil.ImageLoader())
                        .show();
            });

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

package com.example.schoolairdroprefactoredition.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.schoolairdroprefactoredition.R;

public class ImageUtil {

    public static void loadImage(ImageView imageView, String uri, int placeHolderRes, Runnable runWhenImageLoaded) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide.with(imageView).load(uri)
                .apply(requestOptions.placeholder(placeHolderRes))
                .encodeQuality(ConstantUtil.ORIGIN)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        runWhenImageLoaded.run();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        runWhenImageLoaded.run();
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 指定placeholder的矩形
     */
    public static void loadImage(ImageView imageView, String uri, int placeHolderRes) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide.with(imageView).load(uri)
                .apply(requestOptions.placeholder(placeHolderRes))
                .encodeQuality(ConstantUtil.ORIGIN)
                .into(imageView);
    }

    /**
     * 指定placeholder的圆形图片
     */
    public static void loadRoundImage(ImageView imageView, String uri, @DrawableRes int placeholder) {
        Glide.with(imageView).load(uri)
                .circleCrop()
                .apply(new RequestOptions()
                        .encodeQuality(ConstantUtil.ORIGIN)
                        .placeholder(placeholder))
                .into(imageView);
    }

    /**
     * 灰白placeholder的圆角矩形图片
     */
    public static void loadRoundedImage(ImageView imageView, String uri) {
        Glide.with(imageView).load(uri)
                .apply(new RequestOptions()
                        .encodeQuality(ConstantUtil.ORIGIN)
                        .placeholder(R.drawable.placeholder_rounded)
                        .transform(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(5))))
                .into(imageView);
    }

    /**
     * 带主题色placeholder的圆角矩形图片
     */
    public static void loadRoundedImageColorfulPlaceholder(ImageView imageView, String uri) {
        Glide.with(imageView).load(uri)
                .apply(new RequestOptions()
                        .encodeQuality(ConstantUtil.ORIGIN)
                        .placeholder(R.drawable.ic_logo_alpha)
                        .transform(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(5))))
                .into(imageView);
    }

    /**
     * 获取按比例计算的适应屏幕宽度的高度
     *
     * @param resWidth  资源宽度
     * @param resHeight 资源高度
     * @param maxHeight 允许的最大显示高度
     * @return 按比例计算的适应屏幕宽度的高度 或者 最大显示高度
     */
    public static int getResizedHeight(int resWidth, int resHeight, int maxHeight) {
        int screenWidth = ScreenUtils.getAppScreenWidth();
        int resizedHeight = (screenWidth / resWidth) * resHeight;
        return Math.min(resizedHeight, maxHeight);
    }

    /**
     * 将url修正为开头不带 / 的
     */
    public static String fixUrl(String url) {
        if (url == null) {
            return null;
        }

        if (url.startsWith("/")) {
            return url.substring(1);
        } else {
            return url;
        }
    }
}

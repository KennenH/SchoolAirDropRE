package com.example.schoolairdroprefactoredition.utils;

import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.schoolairdroprefactoredition.R;

public class ImageUtil {
    public static void loadImage(ImageView imageView, String uri, int placeHolderRes) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        Glide.with(imageView).load(uri)
                .apply(requestOptions.placeholder(placeHolderRes))
                .encodeQuality(ConstantUtil.ORIGIN)
                .into(imageView);
    }

    public static void loadRoundImage(ImageView imageView, String uri) {
        Glide.with(imageView).load(uri)
                .apply(new RequestOptions()
                        .encodeQuality(ConstantUtil.ORIGIN)
                        .placeholder(R.drawable.ic_logo_alpha)
                        .transform(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(100))))
                .into(imageView);
    }

    public static void loadRoundedImage(ImageView imageView, String uri) {
        Glide.with(imageView).load(uri)
                .apply(new RequestOptions()
                        .encodeQuality(ConstantUtil.ORIGIN)
                        .placeholder(R.drawable.placeholder_rounded)
                        .transform(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(5))))
                .into(imageView);
    }
}

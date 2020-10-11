package com.example.schoolairdroprefactoredition.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtil {
    /**
     * Glide 加载圆角图片
     *
     * @param imageView      目标
     * @param uri            完整的图片地址 包括了base url
     * @param roundedCorners 圆角 in px
     */
    public static void roundedImageLoad(ImageView imageView, String uri, int roundedCorners) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(roundedCorners));
        Glide.with(imageView).load(uri)
                .apply(requestOptions)
                .encodeQuality(80)
                .into(imageView);
    }
}

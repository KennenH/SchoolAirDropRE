package com.example.schoolairdroprefactoredition.utils;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ImageUtil {
    /**
     * 加载固定大小的图片
     */
    public static void scaledImageLoad(SimpleDraweeView simpleDraweeView, String uri, int sideLen) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(sideLen, sideLen))
                .build();
        simpleDraweeView.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(simpleDraweeView.getController())
                        .setImageRequest(request)
                        .build());
    }
}

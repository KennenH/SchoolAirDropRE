package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

public class FrescoEngine implements ImageEngine {
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }

    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }

    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }

    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        ((SimpleDraweeView) imageView).setImageURI(url);
    }


    private FrescoEngine() {
    }

    private static FrescoEngine instance;

    public static FrescoEngine getInstance() {
        if (null == instance) {
            synchronized (FrescoEngine.class) {
                if (null == instance) {
                    instance = new FrescoEngine();
                }
            }
        }
        return instance;
    }
}

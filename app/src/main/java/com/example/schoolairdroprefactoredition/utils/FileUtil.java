package com.example.schoolairdroprefactoredition.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {

    /**
     * 获取路径或者uri指向的文件
     *
     * @param pathOrUri 文件路径或者uri
     * @return 文件
     */
    public static File getFile(String pathOrUri) {
        if (isUriPath(pathOrUri)) {
            return UriUtils.uri2File(Uri.parse(pathOrUri));
        } else {
            return new File(pathOrUri);
        }
    }

    /**
     * 压缩图片转换为base64
     *
     * @param isNeedLarge 是否需要大图，若为头像则只需要小图即可
     */
    @Nullable
    public static File compressFile(String path, boolean isNeedLarge) throws IOException {
        Bitmap bitmap;
        Application application = Utils.getApp();
        if (isUriPath(path)) {
            bitmap = BitmapFactory.decodeStream(application.getContentResolver().openInputStream(Uri.parse(path)));
        } else {
            bitmap = BitmapFactory.decodeFile(path);
        }
        return isNeedLarge ?
                bitmapToFile(application, scaleBitmap(bitmap, 888, 888), 75) :
                bitmapToFile(application, scaleBitmap(bitmap, 350, 350), 90);
    }

    /**
     * 将图片url转换为Bitmap
     */
    @Nullable
    public static Bitmap urlToBitmap(Context context, String imageUrl) {
        final Bitmap[] bitmap = {null};
        Glide.with(context).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bitmap[0] = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
        return bitmap[0];
    }

    /**
     * 将图片大小限制至指定大小
     *
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     */
    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        if (bm == null) {
            return null;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    @Nullable
    public static File bitmapToFile(Context context, Bitmap bitmap, @IntRange(from = 1, to = 100) int quality) {
        if (bitmap == null) return null;

        File f = null;
        try {
            f = File.createTempFile("AddNew_Cover", null, context.getCacheDir());
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        byte[] data = bos.toByteArray();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
            fos.write(data);
            fos.flush();
            fos.close();

            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param key  字段名
     * @param path 图片路径
     * @return MultiPartBody.Part
     */
    @Nullable
    public static MultipartBody.Part createPartWithPath(String key, String path, boolean isNeedLarge) throws IOException {
        File file = compressFile(path, isNeedLarge);
        RequestBody body;
        if (file != null) {
            body = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData(key, file.getName(), body);
        } else {
            return null;
        }
    }

    public static MultipartBody.Builder addFileWithPath(String key, List<String> path, boolean isAddNewItem) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String s : path) {
            File file = compressFile(s, isAddNewItem);
            RequestBody body;
            if (file != null) {
                body = RequestBody.create(MediaType.parse("image/*"), file);
                builder.addFormDataPart(key, file.getName(), body);
            }
        }
        return builder;
    }

    /**
     * 是否为uri
     */
    private static boolean isUriPath(String path) {
        return path.startsWith("content:") || path.startsWith("file:");
    }

    /**
     * bitmap 转换为 uri
     */
    @Nullable
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "sadIWantBackgroundSnapshot",
                null);
        return Uri.parse(path);
    }

    /**
     * url 转换为 bitmap
     */
    @Nullable
    public static Bitmap getBitmapFromURL(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}

package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {
    /**
     * 压缩图片转换为base64
     */
    public static File compressFile(Context context, String path, boolean isAddNewItem) {
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        return isAddNewItem ?
                bitmapToFile(context, scaleBitmap(bitmap, 1080, 1080), 80) :
                bitmapToFile(context, scaleBitmap(bitmap, 350, 350), 100);
    }

    /**
     * 将图片大小限制至指定大小
     *
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     */
    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
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
     * @param context android view model 中的 application context
     * @param key     字段名
     * @param path    图片路径
     * @return MultiPartBody.Part
     */
    @Nullable
    public static MultipartBody.Part createFileWithPath(Context context, String key, String path, boolean isAddNewItem) {
        File file = compressFile(context, path, isAddNewItem);
        RequestBody body;
        if (file != null) {
            body = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData(key, file.getName(), body);
        } else return null;
    }
}

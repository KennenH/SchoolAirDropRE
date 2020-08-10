package com.example.schoolairdroprefactoredition.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowAnimationFrameStats;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.schoolairdroprefactoredition.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class MyUtil {
    /**
     * 屏幕横向平均分配item时需要的margin
     *
     * @param items 一行item数量
     * @param size  item的宽
     * @return margin
     */
    public static int averageItemMargin(int items, int size) {
        return (int) ((ScreenUtils.getAppScreenWidth() - items * size) / (2f * items + 2));
    }

    /**
     * 分页每页固定数量item获取本页数组上限
     *
     * @param total       总共item数量
     * @param itemPerPage 每页item数
     * @param position    当前页
     * @return 当前数组上限（exclusive）
     */
    public static int gridItemBounds(int total, int itemPerPage, int position) {
        return Math.min((position + 1) * itemPerPage, total);
    }

    public static void setupFullHeight(Context context, BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight(context);
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private static int getWindowHeight(Context context) {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void pickPhotoFromAlbum(Activity activity, int requestCode, List<LocalMedia> selected, int max) {
        PictureWindowAnimationStyle animStyle = new PictureWindowAnimationStyle();
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment);
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_white_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureWindowAnimationStyle(animStyle)//相册启动退出动画
                .isZoomAnim(false)
                .maxSelectNum(max)
                .imageSpanCount(3)//列表每行显示个数
                .selectionData(selected)//是否传入已选图片
                .isPreviewImage(false)//是否预览图片
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)
                .forResult(requestCode);
    }

    public static void pickPhotoFromAlbum(Fragment fragment, int requestCode, List<LocalMedia> selected, int max) {
        PictureWindowAnimationStyle animStyle = new PictureWindowAnimationStyle();
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment);
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_white_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureWindowAnimationStyle(animStyle)//相册启动退出动画
                .isZoomAnim(false)
                .maxSelectNum(max)
                .imageSpanCount(3)//列表每行显示个数
                .selectionData(selected)//是否传入已选图片
                .isPreviewImage(false)//是否预览图片
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)
                .forResult(requestCode);
    }

    /**
     * 图片选择器语言
     *
     * @return 中文或英文
     */
    public static int getImagePickerLanguage() {
        Log.d("Language == > ", Locale.getDefault().getLanguage());
        return Locale.getDefault().getLanguage().equals("cn") ? LanguageConfig.CHINESE : LanguageConfig.ENGLISH;
    }
}

package com.example.schoolairdroprefactoredition.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.protocol.ProtocolActivity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import com.luck.picture.lib.tools.SdkVersionUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File


object MyUtil {

    /**
     * 新增物品或者求购选择的最多图片集
     */
    const val ADD_NEW_PIC_SET_MAX = 9

    @JvmStatic
    fun dp2px(dp: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dp(px: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * 将内容复制到粘贴板
     */
    @JvmStatic
    fun copyTpClipboard(context: Context?, content: CharSequence) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("SchoolAirdrop", content)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, context?.getString(R.string.addressCopied, content), Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun loading(context: Context): LoadingPopupView {
        return XPopup.Builder(context)
                .hasShadowBg(false)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .navigationBarColor(context.resources.getColor(R.color.white, context.theme))
                .asLoading()
    }

    @JvmStatic
    fun takePhoto(fragment: Fragment?, requestCode: Int, isCircle: Boolean = false) {
        val animStyle = PictureWindowAnimationStyle()
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment)
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .rotateEnabled(false)
                .isCompress(true)
                .minimumCompressSize(100) // 小于多少kb的图片不压缩
                .theme(R.style.picture_white_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureWindowAnimationStyle(animStyle) //相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)
                .forResult(requestCode)
    }

    @JvmStatic
    fun takePhoto(activity: Activity?, requestCode: Int, isCircle: Boolean = false) {
        val animStyle = PictureWindowAnimationStyle()
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment)
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .circleDimmedLayer(isCircle)
                .rotateEnabled(false)
                .isCompress(true)
                .minimumCompressSize(100) // 小于多少kb的图片不压缩
                .theme(R.style.picture_white_style)
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureWindowAnimationStyle(animStyle) //相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)
                .forResult(requestCode)
    }

    /**
     * @param isSquare 是否正方形剪裁
     * @param isCircle 是否圆形剪裁
     * @param isCropWithoutSpecificShape 是否剪裁但不指定形状
     */
    @JvmStatic
    fun pickPhotoFromAlbum(activity: Activity?, requestCode: Int, maxSelect: Int, isSquare: Boolean = false, isCircle: Boolean = false, isCropWithoutSpecificShape: Boolean = false) {
        val animStyle = PictureWindowAnimationStyle()
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment)
        val model = PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .theme(R.style.picture_white_style) // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setPictureWindowAnimationStyle(animStyle) // 自定义相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION) // 列表动画效果
                .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(maxSelect) // 最大图片选择数量
                .minSelectNum(0) // 最小选择数量
                .imageSpanCount(3) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                //                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q()) // 如果视频有旋转角度则对换宽高,默认为false
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                //                .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                //                .selectionMode(maxSelect == 1 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(false) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isPreviewVideo(false) // 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isCamera(true) // 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(false) // 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .synOrAsy(true) //同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                .isEnableCrop(isSquare || isCropWithoutSpecificShape) // 启用剪裁
                .hideBottomControls(false) // 是否显示uCrop工具栏，默认不显示
                .isGif(false) // 是否显示gif图片
                .freeStyleCropEnabled(false) // 裁剪框是否可拖拽
                .circleDimmedLayer(isCircle) // 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                //                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isOpenClickSound(false) // 是否开启点击声音
                .rotateEnabled(false)
        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
        //.videoMinSecond(10)// 查询多少秒以内的视频
        //.videoMaxSecond(15)// 查询多少秒以内的视频
        //.recordVideoSecond(10)//录制视频秒数 默认60s
        //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
        //                .cutOutQuality(60)// 裁剪输出质量 默认100
        //                .minimumCompressSize(250)// 小于多少kb的图片不压缩
        //                .cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//        .rotateEnabled(false) // 裁剪是否可旋转图片
        //.scaleEnabled(false)// 裁剪是否可放大缩小图片
        //.videoQuality()// 视频录制质量 0 or 1
        //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        if (isSquare) {
            model.withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
        }

        model.forResult(requestCode)
    }

    @JvmStatic
    fun pickPhotoFromAlbum(fragment: Fragment?, requestCode: Int, max: Int, isSquare: Boolean = false, isCircle: Boolean = false, isCropWithoutSpecificShape: Boolean = false) {
        val animStyle = PictureWindowAnimationStyle()
        animStyle.ofAllAnimation(R.anim.enter_y_fragment, R.anim.popexit_y_fragment)
        val model = PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .theme(R.style.picture_white_style) // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setPictureWindowAnimationStyle(animStyle) // 自定义相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION) // 列表动画效果
                .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(max) // 最大图片选择数量
                .minSelectNum(0) // 最小选择数量
                .imageSpanCount(3) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                //                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q()) // 如果视频有旋转角度则对换宽高,默认为false
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                //                .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                //                .selectionMode(max == 1 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(false) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isPreviewVideo(false) // 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isCamera(true) // 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(false) // 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                .isEnableCrop(isSquare || isCropWithoutSpecificShape) // 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                //                .isCompress(true)// 是否压缩
                .rotateEnabled(false) // 旋转
                //                .compressQuality(60)// 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                .hideBottomControls(false) // 是否显示uCrop工具栏，默认不显示
                .isGif(false) // 是否显示gif图片
                .freeStyleCropEnabled(false) // 裁剪框是否可拖拽
                .circleDimmedLayer(isCircle) // 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                //                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isOpenClickSound(false) // 是否开启点击声音
                .rotateEnabled(false)
        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
        //.videoMinSecond(10)// 查询多少秒以内的视频
        //.videoMaxSecond(15)// 查询多少秒以内的视频
        //.recordVideoSecond(10)//录制视频秒数 默认60s
        //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
        //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
        //                .cutOutQuality(90)// 裁剪输出质量 默认100
        //                .minimumCompressSize(250)// 小于多少kb的图片不压缩
        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
        //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
        //.scaleEnabled(false)// 裁剪是否可放大缩小图片
        //.videoQuality()// 视频录制质量 0 or 1
        //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        if (isSquare) {
            model.withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
        }

        model.forResult(requestCode)
    }

    class ImageLoader : XPopupImageLoader {
        override fun loadImage(position: Int, url: Any, imageView: ImageView) {
            Glide.with(imageView)
                    .load(url)
                    .encodeQuality(ConstantUtil.ORIGIN)
                    .apply(RequestOptions().placeholder(R.drawable.logo_placeholder))
                    .into(imageView)
        }

        override fun getImageFile(context: Context, uri: Any): File? {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    /**
     * 打开隐私政策和用户协议页面
     */
    @JvmStatic
    fun openPrivacyWebsite(context: Context?) {
        ProtocolActivity.start(context, ProtocolActivity.PROTOCOL_PRIVACY)
    }

    /**
     * 打开服务条款页面
     */
    @JvmStatic
    fun openServiceWebsite(context: Context?) {
        ProtocolActivity.start(context, ProtocolActivity.PROTOCOL_USER)
    }
}
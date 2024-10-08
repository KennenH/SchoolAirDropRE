package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.example.schoolairdroprefactoredition.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class DialogUtil {

    /**
     * 显示确认对话框
     *
     * @param title           标题
     * @param content         内容
     * @param confirmListener 确认后的事件
     */
    public static void showConfirm(Context context, CharSequence title, CharSequence content, OnConfirmListener confirmListener) {
        if (content == null) return;

        boolean isDarkTheme = false;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            isDarkTheme = true;
        }

        new XPopup.Builder(context)
                .isDarkTheme(isDarkTheme)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asConfirm(title, content, context.getString(android.R.string.cancel), context.getString(android.R.string.ok)
                        , confirmListener, () -> {
                        }, false).show();
    }

    /**
     * 在屏幕中央显示消息提示框
     *
     * @param type one of {@link DIALOG_TYPE}
     */
    public static void showCenterDialog(Context context, @DIALOG_TYPE int type, @StringRes int tip) {
        if (context == null) return;

        switch (type) {
            case DialogUtil.DIALOG_TYPE.SUCCESS:
                showSuccess(context, tip);
                break;
            case DialogUtil.DIALOG_TYPE.FAILED:
                showFailed(context, tip);
                break;
            case DialogUtil.DIALOG_TYPE.ERROR_NETWORK:
                showNetWorkError(context, tip);
                break;
            case DIALOG_TYPE.FAVOR:
                showFavor(context, tip);
                break;
            default:
                showUnknown(context, tip);
                break;
        }
    }

    /**
     * 对话框类型
     */
    public @interface DIALOG_TYPE {
        int SUCCESS = 123;
        int FAILED = 234;
        int ERROR_UNKNOWN = 345;
        int ERROR_NETWORK = 456;
        int FAVOR = 567;
    }

    /**
     * 显示操作成功提示框
     */
    private static void showSuccess(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asCustom(new BasePopupView(context) {
                    @Override
                    protected void init() {
                        super.init();
                        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
                    }

                    @Override
                    protected int getPopupLayoutId() {
                        return R.layout.dialog_center_success;
                    }
                }).show().delayDismissWith(1000, () -> {
        });
    }

    /**
     * 显示操作失败提示框
     */
    private static void showFailed(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asCustom(new BasePopupView(context) {
                    @Override
                    protected void init() {
                        super.init();
                        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
                    }

                    @Override
                    protected int getPopupLayoutId() {
                        return R.layout.dialog_center_failed;
                    }
                }).show().delayDismissWith(1000, () -> {
        });
    }

    /**
     * 显示网络错误提示框
     */
    private static void showNetWorkError(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asCustom(new BasePopupView(context) {
                    @Override
                    protected void init() {
                        super.init();
                        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
                    }

                    @Override
                    protected int getPopupLayoutId() {
                        return R.layout.dialog_center_error_network;
                    }
                }).show().delayDismissWith(1000, () -> {
        });
    }

    /**
     * 显示收藏结果提示框
     */
    private static void showFavor(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asCustom(new BasePopupView(context) {
                    @Override
                    protected void init() {
                        super.init();
                        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
                    }

                    @Override
                    protected int getPopupLayoutId() {
                        return R.layout.dialog_center_favorite;
                    }
                }).show().delayDismissWith(1000, () -> {
        });
    }

    /**
     * 显示未知错误提示框
     */
    private static void showUnknown(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
                .hasShadowBg(false)
                .navigationBarColor(context.getResources().getColor(R.color.white, context.getTheme()))
                .asCustom(new BasePopupView(context) {
                    @Override
                    protected void init() {
                        super.init();
                        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
                    }

                    @Override
                    protected int getPopupLayoutId() {
                        return R.layout.dialog_center_attention;
                    }
                }).show().delayDismissWith(1000, () -> {
        });
    }
}

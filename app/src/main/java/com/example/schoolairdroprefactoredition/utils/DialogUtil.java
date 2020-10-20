package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class DialogUtil {
    public static void showConfirm(Context context, CharSequence title, CharSequence content, OnConfirmListener confirmListener) {
        boolean isDarkTheme = false;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            isDarkTheme = true;
        }

        new XPopup.Builder(context)
                .isDarkTheme(isDarkTheme)
                .asConfirm(title, content, context.getString(android.R.string.cancel), context.getString(android.R.string.ok)
                        , confirmListener, () -> {
                        }, false).show();
    }

    /**
     * 对话框类型
     */
    public @interface DIALOG_TYPE {
        int SUCCESS = 123;
        int FAILED = 234;
        int ERROR_UNKNOWN = 345;
        int ERROR_NETWORK = 456;
    }

    /**
     * 在屏幕中央显示消息提示对话框
     *
     * @param type one of {@link DIALOG_TYPE}
     */
    public static void showCenterDialog(Context context, @DIALOG_TYPE int type, @StringRes int tip) {
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
            default:
                showUnknown(context, tip);
                break;
        }
    }

    private static void showSuccess(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
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
                }).show().delayDismiss(1000);
    }

    private static void showFailed(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
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
                }).show().delayDismiss(1000);
    }

    private static void showNetWorkError(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
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
                }).show().delayDismiss(1000);
    }

    private static void showUnknown(Context context, @StringRes int tip) {
        new XPopup.Builder(context)
                .isClickThrough(true)
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
                }).show().delayDismiss(1000);
    }
}

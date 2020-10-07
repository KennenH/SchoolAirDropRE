package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;

import androidx.annotation.StringRes;

import com.example.schoolairdroprefactoredition.ui.components.CenterDialogPopView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class DialogUtil {
    public static void showConfirm(Context context, CharSequence title, CharSequence content, OnConfirmListener confirmListener) {
        new XPopup.Builder(context).asConfirm(title, content, context.getString(android.R.string.cancel), context.getString(android.R.string.ok)
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
        new XPopup.Builder(context).asCustom(new CenterDialogPopView(context, tip, type)).show().delayDismiss(1000);
    }
}

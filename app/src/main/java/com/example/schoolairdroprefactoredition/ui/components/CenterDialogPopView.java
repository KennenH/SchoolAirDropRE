package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.lxj.xpopup.core.BasePopupView;

public class CenterDialogPopView extends BasePopupView {
    @DialogUtil.DIALOG_TYPE
    private int type;

    public CenterDialogPopView(@NonNull Context context, @StringRes int tip, @DialogUtil.DIALOG_TYPE int type) {
        super(context);
        ((TextView) findViewById(R.id.dialog_txt)).setText(tip);
        this.type = type;
    }

    @Override
    protected int getPopupLayoutId() {
        switch (type) {
            case DialogUtil.DIALOG_TYPE.SUCCESS:
                return R.layout.dialog_center_success;
            case DialogUtil.DIALOG_TYPE.FAILED:
                return R.layout.dialog_center_failed;
            case DialogUtil.DIALOG_TYPE.ERROR_NETWORK:
                return R.layout.dialog_center_error_network;
            default:
                return R.layout.dialog_center_attention;
        }
    }
}

package com.example.schoolairdroprefactoredition.scene.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

import me.jessyan.autosize.AutoSizeCompat;

public class ImmersionStatusBarActivity extends AppCompatActivity {
    private LoadingPopupView mLoading;
    protected boolean isDarkTheme = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            isDarkTheme = true;
        }

        setActivityTheme();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setActivityTheme() {
        StatusBarUtil.setStatusTextColor(this, getColor(R.color.white), !isDarkTheme);
        BarUtils.setStatusBarLightMode(this, !isDarkTheme);
        BarUtils.setNavBarLightMode(this, !isDarkTheme);
        BarUtils.setNavBarColor(this, getColor(R.color.white));
    }

    protected void showLoading() {
        if (mLoading == null)
            mLoading = MyUtil.loading(this);

        mLoading.show();
    }

    protected void dismissLoading() {
        if (mLoading != null)
            mLoading.smartDismiss();
    }

    protected void dismissLoading(Runnable task) {
        if (mLoading != null)
            mLoading.dismissWith(task);
    }

    /**
     * 帮助androidAutoSize适配屏幕
     */
    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.getResources();
    }
}

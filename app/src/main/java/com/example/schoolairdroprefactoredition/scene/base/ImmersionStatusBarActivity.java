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

    /**
     * 是否为暗黑模式
     */
    protected boolean isDarkTheme = false;

    /**
     * 是否有请求正在运行标志位，当有请求正在运行时通过
     * {@link ImmersionStatusBarActivity#showLoading(Runnable)}
     * 运行的网络请求将不会再次发起并且会被抛弃而不是等待上一个请求结束
     */
    private boolean isRequestRunning = false;

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
        if (mLoading == null) {
            mLoading = MyUtil.loading(this);
        }

        mLoading.show();
    }

    /**
     * request在获取数据结束之前将只会被发起一次
     *
     * @param request 网络请求
     */
    protected void showLoading(Runnable request) {
        if (mLoading == null) {
            mLoading = MyUtil.loading(this);
        }

        mLoading.show();

        if (!isRequestRunning) {
            request.run();
        }
    }

    protected void dismissLoading() {
        isRequestRunning = false;

        if (mLoading != null) {
            mLoading.smartDismiss();
        }
    }

    protected void dismissLoading(Runnable task) {
        isRequestRunning = false;

        if (mLoading != null) {
            mLoading.dismissWith(task);
        }
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

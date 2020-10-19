package com.example.schoolairdroprefactoredition.scene.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

public class ImmersionStatusBarActivity extends AppCompatActivity {
    private LoadingPopupView mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBar();
        setBottomNavBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    protected void setStatusBar() {
//        BarUtils.setStatusBarColor(this, Color.WHITE);
//        BarUtils.setStatusBarLightMode(this, true);
        StatusBarUtil.setStatusTextColor(this, Color.WHITE);
    }

    protected void setBottomNavBar() {
        BarUtils.setNavBarColor(this, Color.WHITE);
        BarUtils.setNavBarLightMode(this, true);
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
}

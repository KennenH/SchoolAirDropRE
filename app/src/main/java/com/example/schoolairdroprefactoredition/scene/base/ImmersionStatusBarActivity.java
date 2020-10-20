package com.example.schoolairdroprefactoredition.scene.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.lxj.xpopup.impl.LoadingPopupView;

public class ImmersionStatusBarActivity extends AppCompatActivity {
    private LoadingPopupView mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBar();
        setBottomNavBar();
        setThemeMode();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    protected void setThemeMode() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_NO:
                BarUtils.setNavBarLightMode(this, true);
                BarUtils.setStatusBarLightMode(this, true);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                BarUtils.setNavBarLightMode(this, false);
                BarUtils.setStatusBarLightMode(this, false);
                break;
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setStatusTextColor(this, getResources().getColor(R.color.white, getTheme()));
    }

    protected void setBottomNavBar() {
        BarUtils.setNavBarColor(this, getResources().getColor(R.color.white, getTheme()));
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

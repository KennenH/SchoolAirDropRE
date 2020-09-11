package com.example.schoolairdroprefactoredition.scene.base;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;

public class ImmersionStatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setBottomNavBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setStatusTextColorBlack(this, Color.WHITE);
    }

    protected void setBottomNavBar() {
        BarUtils.setNavBarColor(this, Color.WHITE);
        BarUtils.setNavBarLightMode(this, true);
    }
}

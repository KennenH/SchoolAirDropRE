package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.home.HomeFragment;
import com.example.schoolairdroprefactoredition.fragment.my.MyFragment;
import com.example.schoolairdroprefactoredition.fragment.purchasing.PurchasingFragment;
import com.example.schoolairdroprefactoredition.fragment.search.SearchFragment;
import com.example.schoolairdroprefactoredition.utils.ColorUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaeger.library.StatusBarUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private HomeFragment mHome;
    private PurchasingFragment mPurchase;
    private MyFragment mMy;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        setStatusBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 添加新的tab时添加
        mHome = new HomeFragment();
        mPurchase = new PurchasingFragment();
        mMy = new MyFragment();

        mPurchase.setOnSearchBarClickListener(() -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SearchFragment(/* parameters such as home or purchase */))
                    .addToBackStack(null)
                    .commit();
        });
        mHome.setOnSearchBarClickListener(() -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SearchFragment())
                    .addToBackStack(null)
                    .commit();
        });
        navView.setOnNavigationItemSelectedListener(this);
        showFragment(mHome);
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucent(this, 0);
            if (ColorUtil.isColorDark(getResources().getColor(R.color.primary, getTheme()))) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            StatusBarUtil.setTranslucent(this, 66);
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment.isHidden() || !fragment.isAdded()) {
            mFragmentManager
                    .beginTransaction()
                    // 添加新的tab时添加
                    .hide(mHome)
                    .hide(mMy)
                    .hide(mPurchase)
                    .commit();

            if (!fragment.isAdded()) {
                int mContainer = R.id.nav_host_fragment;
                mFragmentManager
                        .beginTransaction()
                        .add(mContainer, fragment)
                        .show(fragment)
                        .commit();
            } else {
                mFragmentManager
                        .beginTransaction()
                        .show(fragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 添加新的tab时添加
        mHome = null;
        mMy = null;
        mPurchase = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 添加新的tab时添加
            case R.id.navigation_home:
                showFragment(mHome);
                return true;
            case R.id.navigation_my:
                showFragment(mMy);
                return true;
            case R.id.navigation_box:
                showFragment(mPurchase);
                return true;
        }
        return false;
    }
}

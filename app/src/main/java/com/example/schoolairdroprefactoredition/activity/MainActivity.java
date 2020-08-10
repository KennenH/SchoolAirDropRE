package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.home.HomeFragment;
import com.example.schoolairdroprefactoredition.fragment.my.MyFragment;
import com.example.schoolairdroprefactoredition.fragment.purchasing.PurchasingFragment;
import com.example.schoolairdroprefactoredition.fragment.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends ImmersionStatusBarActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
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

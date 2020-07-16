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
import com.example.schoolairdroprefactoredition.fragment.search.SearchFragment;
import com.example.schoolairdroprefactoredition.utils.ColorUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaeger.library.StatusBarUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private HomeFragment mHome;
    private MyFragment mMy;
    private SearchFragment mSearch;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        setStatusBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        mHome = new HomeFragment();
        mMy = new MyFragment();

        mHome.setOnSearchBarClickListener(() -> {
            mSearch = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mSearch)
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
                    .hide(mHome)
                    .hide(mMy)
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
        mHome = null;
        mMy = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showFragment(mHome);
                return true;
            case R.id.navigation_my:
                showFragment(mMy);
                return true;
        }
        return false;
    }
}

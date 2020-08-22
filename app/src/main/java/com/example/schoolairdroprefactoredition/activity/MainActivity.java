package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.fragment.home.ParentNewsFragment;
import com.example.schoolairdroprefactoredition.fragment.my.MyFragment;
import com.example.schoolairdroprefactoredition.fragment.home.ParentPurchasingFragment;
import com.example.schoolairdroprefactoredition.fragment.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import me.jessyan.autosize.AutoSizeCompat;

public class MainActivity extends ImmersionStatusBarActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AMapLocationListener {
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private OnLocationListener mOnLocationListener;
    private OnLoginActivityListener mOnLoginActivityListener;

    private ParentNewsFragment mHome;
    private ParentPurchasingFragment mPurchase;
    private MyFragment mMy;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // 添加新的tab时在此添加
        if (mHome == null) {
            mHome = new ParentNewsFragment();
            mHome.setOnSearchBarClickedListener(() -> mFragmentManager.beginTransaction()
                    .replace(R.id.container, new SearchFragment())
                    .addToBackStack(null)
                    .commit());
        }
        if (mPurchase == null) {
            mPurchase = new ParentPurchasingFragment();
            mPurchase.setOnSearchBarClickedListener(() -> mFragmentManager.beginTransaction()
                    .replace(R.id.container, new SearchFragment())
                    .addToBackStack(null)
                    .commit());
        }
        if (mMy == null)
            mMy = new MyFragment();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        showFragment(mHome);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SettingsActivity.REQUEST_LOGIN) { // 来自SettingsActivity的登录结果返回
                if (data != null && mOnLoginActivityListener != null) {
                    mOnLoginActivityListener.onLoginActivity(data.getExtras());
                }
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     * 白色 字体黑色
     */
    @Override
    protected void setStatusBar() {

    }

    /**
     * 定位
     * 结果回调至
     * {@link ParentPurchasingFragment}
     * {@link ParentNewsFragment}
     */
    public void startLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationClient.setLocationListener(this);
        }
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationCacheEnable(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
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


    //============== Location listener begin ================
    // 定位成功后的回调监听
    public interface OnLocationListener {
        void onLocated(AMapLocation aMapLocation);
    }

    public void setOnLocationListener(OnLocationListener listener) {
        mOnLocationListener = listener;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationListener != null)
            mOnLocationListener.onLocated(aMapLocation);
    }
    //============== Location listener end ================


    //============== Login listener begin =================
    //登录成功后的回调监听
    public interface OnLoginActivityListener {
        void onLoginActivity(Bundle bundle);
    }

    public void setOnLoginActivityListener(OnLoginActivityListener listener) {
        mOnLoginActivityListener = listener;
    }
    //=============== Login listener end ==================


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 添加新的tab时添加
        mHome = null;
        mMy = null;
        mPurchase = null;

        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
            mOnLocationListener = null;
        }
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

    /**
     * 帮助AndroidAutoSize适配屏幕
     *
     * @return
     */
    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
        return super.getResources();
    }

}

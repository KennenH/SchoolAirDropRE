package com.example.schoolairdroprefactoredition.scene.main;

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
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.scene.main.home.ParentNewsFragment;
import com.example.schoolairdroprefactoredition.scene.main.my.MyFragment;
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPurchasingFragment;
import com.example.schoolairdroprefactoredition.scene.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import me.jessyan.autosize.AutoSizeCompat;

public class MainActivity extends PermissionBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AMapLocationListener {

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private OnLoginActivityListener mOnLoginActivityListener;
    protected OnLocationListener mOnLocationListener;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;

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

    /**
     * 定位
     * 结果回调至
     * {@link ParentPurchasingFragment}
     * {@link ParentNewsFragment}
     */
    private void startLocation() {
        if (mClient == null) {
            mClient = new AMapLocationClient(this);
            mClient.setLocationListener(this);
        }
        if (mOption == null) {
            mOption = new AMapLocationClientOption();
            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
    }

    @Override
    protected void locationGranted() {
        super.locationGranted();
        startLocation();
    }

    @Override
    protected void locationDenied() {
        super.locationDenied();
        mOnLocationListener.onPermissionDenied();
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

    /**
     * 登录结果回调至子fragment
     * 登录流程详见{@link OnLoginActivityListener}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) { // 来自SettingsActivity的登录结果返回,去向子fragment的监听回调
                if (data != null && mOnLoginActivityListener != null) {
                    mOnLoginActivityListener.onLoginActivity(data.getExtras());
                }
            }
        }
    }

    // 定位成功后的回调监听
    public interface OnLocationListener {
        void onLocated(AMapLocation aMapLocation);

        void onPermissionDenied();
    }

    public void setOnLocationListener(OnLocationListener listener) {
        mOnLocationListener = listener;
    }


    /**
     * 登录成功后的回调监听
     * 整个流程为:
     * .                  监听
     * {@link MyFragment} ===> {@link MainActivity}
     * .                                                                                         监听
     * {@link com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment} === > {@link SettingsActivity}
     * <p>
     * .                     start for result                          start for result
     * {@link MainActivity} ================> {@link SettingsActivity} ================> {@link com.example.schoolairdroprefactoredition.scene.settings.LoginActivity}
     * <p>
     * 再按原路返回至各个Activity,然后监听回调至{@link MyFragment} 与 {@link com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment}
     * <p>
     */
    public interface OnLoginActivityListener {
        void onLoginActivity(Bundle bundle);
    }

    public void setOnLoginActivityListener(OnLoginActivityListener listener) {
        mOnLoginActivityListener = listener;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationListener != null)
            mOnLocationListener.onLocated(aMapLocation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 添加新的tab时添加
        mHome = null;
        mMy = null;
        mPurchase = null;

        if (mClient != null) {
            mClient.onDestroy();
            mClient = null;
            mOption = null;
        }
        mOnLoginActivityListener = null;
        mOnLocationListener = null;
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
     */
    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
        return super.getResources();
    }

}

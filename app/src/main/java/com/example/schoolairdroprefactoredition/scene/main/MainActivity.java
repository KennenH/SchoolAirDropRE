package com.example.schoolairdroprefactoredition.scene.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.home.ParentNewsFragment;
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPurchasingFragment;
import com.example.schoolairdroprefactoredition.scene.main.my.MyFragment;
import com.example.schoolairdroprefactoredition.scene.search.SearchFragment;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginViewModel;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import me.jessyan.autosize.AutoSizeCompat;

public class MainActivity extends PermissionBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AMapLocationListener, LoginViewModel.OnLoginErrorListener {
    private LoginViewModel loginViewModel;
    private TokenViewModel tokenViewModel;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    protected OnLocationListener mOnLocationListener;
    private OnLoginStateChangedListener mOnLoginStateChangedListener;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;

    private ParentNewsFragment mHome;
    private ParentPurchasingFragment mPurchase;
    private MyFragment mMy;

    private Bundle bundle = new Bundle();

    private static boolean autoLogged = false; // 标识是否已自动登录，防止多个子fragment多次调用

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        tokenViewModel = new ViewModelProvider(this).get(TokenViewModel.class);
        loginViewModel.setOnLoginErrorListener(this);

        // 获取本地用户信息缓存
        tokenViewModel.getTokenCaChe().observe(this, token -> bundle.putSerializable(ConstantUtil.KEY_AUTHORIZE, token));
        tokenViewModel.getUserInfoCache().observe(this, info -> bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info));

        // 添加新的tab时在此添加
        if (mHome == null) {
            mHome = ParentNewsFragment.newInstance(bundle);
            mHome.setOnSearchBarClickedListener(() -> mFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(bundle))
                    .addToBackStack(null)
                    .commit());
        }
        if (mPurchase == null) {
            mPurchase = ParentPurchasingFragment.newInstance(bundle);
            mPurchase.setOnSearchBarClickedListener(() -> mFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(bundle))
                    .addToBackStack(null)
                    .commit());
        }
        if (mMy == null)
            mMy = MyFragment.newInstance(bundle);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        showFragment(mHome);
    }

    /**
     * 在有本地用户token时自动登录
     * 如果获取本地token为空而用户基本信息仍有缓存则代表用户token已过期，将自动重新登录
     * <p>
     * token 有效时间在LoginImpl中修改，否则与服务器过期时间不同会导致用户自动登录失败
     */
    public void autoLogin() {
        if (!autoLogged) {
            autoLogged = true; // 已自动登录标识，防止多个子fragment调用此方法

            tokenViewModel.getTokenCaChe().observe(this, tokenCache -> {
                if (tokenCache != null) { // token 仍有效 使用本地缓存重新获取token后登录
                    autoLoginWithToken();
                } else {
                    tokenViewModel.getUserInfoCache().observe(this, infoCache -> {
                        if (infoCache != null) // token 已无效 使用本地缓存重新获取token后登录
                            autoReLoginWithCache();
                    });
                }
            });
        }
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
        if (mOnLocationListener != null)
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
     * 登录 退登 用户信息修改
     * 登录流程详见{@link OnLoginStateChangedListener}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LoginActivity.LOGIN: // 登录返回
                    if (data != null) {
                        bundle.putAll(data.getExtras());
                        if (mOnLoginStateChangedListener != null)
                            mOnLoginStateChangedListener.onLoginStateChanged(bundle);
                    }
                    break;
                case UserActivity.REQUEST_UPDATE: // 用户信息修改返回:
                    if (data != null && data.getBooleanExtra(ConstantUtil.KEY_UPDATED, false))
                        autoLoginWithToken();
                    break;
                case SettingsFragment.LOGOUT: // 退出登录返回:
                    bundle = new Bundle();
                    if (mOnLoginStateChangedListener != null)
                        mOnLoginStateChangedListener.onLoginStateChanged(bundle);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onLoginError() {
        dismissLoading();
        MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.FAILED);
    }

    /**
     * 定位成功后的回调监听
     */
    public interface OnLocationListener {
        void onLocated(AMapLocation aMapLocation);

        void onPermissionDenied();
    }

    public void setOnLocationListener(OnLocationListener listener) {
        mOnLocationListener = listener;
    }


    /**
     * todo 可以将所有登录回调获取登录信息的地方都换成在缓存中获取，统一数据的可信来源是解决页面状态不一致的最佳办法
     * <p>
     * 登录状态改变后的后的回调监听 登录 退登 都会回到此处
     * SettingsActivity中登录流程为:
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
     * <p>
     * 退出登录后将本地token清除 同时清除页面用户信息
     */
    public interface OnLoginStateChangedListener {
        void onLoginStateChanged(@NotNull Bundle bundle);
    }

    public void setOnLoginActivityListener(OnLoginStateChangedListener listener) {
        mOnLoginStateChangedListener = listener;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationListener != null)
            mOnLocationListener.onLocated(aMapLocation);

        bundle.putDouble(ConstantUtil.LONGITUDE, aMapLocation.getLongitude());
        bundle.putDouble(ConstantUtil.LATITUDE, aMapLocation.getLatitude());
    }

    /**
     * 使用既有的用户基本信息缓存获取token
     */
    private void autoReLoginWithCache() {

        Toast.makeText(this, "token 已过期 正在尝试重新登录", Toast.LENGTH_SHORT).show();

        DomainUserInfo.DataBean info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        if (info != null)
            loginViewModel.getPublicKey().observe(this, key -> {
                loginViewModel.authorizeWithAlipayID(
                        key.getCookie()
                        , info.getUalipay()
                        , key.getPublic_key())
                        .observe(this, token -> {
                            bundle.putSerializable(ConstantUtil.KEY_AUTHORIZE, token);
                            autoLoginWithToken();
                        });
            });
    }

    public Bundle getBundle() {
        return bundle;
    }

    /**
     * 使用token换取用户信息
     * 在用户修改信息后调用
     */
    private void autoLoginWithToken() {
        DomainAuthorize token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        if (token != null && token.getAccess_token() != null) {
            loginViewModel.getUserInfo(token.getAccess_token()).observe(this, data -> {

                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

                DomainUserInfo.DataBean userInfo = data.getData().get(0);
                bundle.putSerializable(ConstantUtil.KEY_USER_INFO, userInfo);
                if (mOnLoginStateChangedListener != null)
                    mOnLoginStateChangedListener.onLoginStateChanged(bundle);
            });
        }
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
        mOnLoginStateChangedListener = null;
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

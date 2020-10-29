package com.example.schoolairdroprefactoredition.scene.main

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.main.home.ParentNewsFragment
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPurchasingFragment
import com.example.schoolairdroprefactoredition.scene.main.my.MyFragment
import com.example.schoolairdroprefactoredition.scene.search.SearchFragment
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginViewModel
import com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModelKt
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mob.pushsdk.MobPush
import kotlinx.android.synthetic.main.activity_main.*
import me.jessyan.autosize.AutoSizeCompat

class MainActivityKt : PermissionBaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        AMapLocationListener {

    companion object {
        var autoLogged = false
    }

    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModelKt::class.java)
    }

    private val tokenViewModel by lazy {
        ViewModelProvider(this).get(TokenViewModel::class.java)
    }

    private val mClient by lazy {
        AMapLocationClient(this@MainActivityKt)
    }

    private val mOption by lazy {
        AMapLocationClientOption()
    }

    private val mHome by lazy {
        ParentNewsFragment()
    }

    private val mMy by lazy {
        MyFragment()
    }

    private val mPurchasing by lazy {
        ParentPurchasingFragment()
    }

    private var mOnLoginStateChangedListener: OnLoginStateChangedListener? = null
    private var mOnLocationListener: OnLocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        initCache()
        initView()
    }

    /**
     * 当app可见时检查当前页面与导航栏指向页面一致
     */
    override fun onResume() {
        super.onResume()
        when {
            mHome.isVisible -> navView.selectedItemId = R.id.navigation_home
            mPurchasing.isVisible -> navView.selectedItemId = R.id.navigation_box
            mPurchasing.isVisible -> navView.selectedItemId = R.id.navigation_my
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                LoginActivity.LOGIN -> {
                    intent.extras?.putAll(data?.extras)
                    mOnLoginStateChangedListener?.onLoginStateChanged()
                }

                UserActivity.REQUEST_UPDATE -> {
                    if (data?.getBooleanExtra(ConstantUtil.KEY_UPDATED, false) as Boolean)
                        autoLoginWithToken()
                }

                SettingsFragment.LOGOUT -> {
                    intent.extras?.clear()
                    mOnLoginStateChangedListener?.onLoginStateChanged()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        mClient.stopLocation()
        mClient.unRegisterLocationListener(this@MainActivityKt)

        mOnLocationListener = null
        mOnLoginStateChangedListener = null
    }

    private fun initView() {
        mHome.setOnSearchBarClickedListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(intent.extras))
                    .addToBackStack(null)
                    .commit()
        }

        mPurchasing.setOnSearchBarClickedListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance(intent.extras))
                    .addToBackStack(null)
                    .commit()
        }

        navView.setOnNavigationItemSelectedListener(this@MainActivityKt)
        navView.selectedItemId = R.id.navigation_box
    }

    override fun locationGranted() {
        super.locationGranted()
        startLocation()
    }

    override fun locationDenied() {
        super.locationDenied()
        mOnLocationListener?.onPermissionDenied()
    }

    private fun initCache() {
        tokenViewModel.tokenCaChe.observe(this@MainActivityKt, {
            intent.putExtra(ConstantUtil.KEY_AUTHORIZE, it)
        })
        tokenViewModel.userInfoCache.observe(this@MainActivityKt, {
            intent.putExtra(ConstantUtil.KEY_USER_INFO, it)
        })
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            if (fragment.isHidden || !fragment.isAdded) {
                beginTransaction()
                        .hide(mHome)
                        .hide(mMy)
                        .hide(mPurchasing)
                        .commit()

                if (!fragment.isAdded)
                    beginTransaction()
                            .add(R.id.navHostFragment, fragment)
                            .show(fragment)
                            .commit()
                else
                    beginTransaction()
                            .show(fragment)
                            .commit()
            }
        }
    }

    /**
     * 在有本地用户token时自动登录
     * 如果获取本地token为空而用户基本信息仍有缓存则代表用户token已过期，将自动重新登录
     * <p>
     * token 有效时间在LoginImpl中修改，否则与服务器过期时间不同会导致用户自动登录失败
     */
    fun autoLogin() {
        if (autoLogged) {
            autoLogged = true // 已自动登录标识，防止多个子fragment调用此方法
            tokenViewModel.tokenCaChe.observe(this, {
                if (it != null) { // token 仍有效 使用本地缓存重新获取token后登录
                    autoLoginWithToken()
                } else {
                    tokenViewModel.userInfoCache.observe(this, { infoCache: DomainUserInfo.DataBean? ->
                        if (infoCache != null) // token 已无效 使用本地缓存重新获取token后登录
                            autoReLoginWithCache()
                    })
                }
            })
        }
    }

    /**
     * 用当前获取的公钥加密alipay并登录
     * 在下一情形调用
     * 1、用户手动登录
     * 2、用户信息修改后回到MainActivity
     */
    private fun autoLoginWithToken() {
        val token: DomainAuthorize = intent.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE) as DomainAuthorize
        if (token.access_token != null) {
            loginViewModel.getUserInfo(token.access_token).observe(this, {

                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show()

                val userInfo = it?.data?.get(0)
                intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
                mOnLoginStateChangedListener?.onLoginStateChanged()
            })
        }
    }

    /**
     * 使用已有的缓存进行登录
     * 在app首次打开时调用
     */
    private fun autoReLoginWithCache() {
        val userInfo: DomainUserInfo.DataBean = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean
        if (userInfo.ualipay != null) {
            loginViewModel.apply {
                getPublicKey().observe(this@MainActivityKt, { publicK ->
                    authorizeWithAlipayID(
                            publicK.cookie,
                            userInfo.ualipay,
                            publicK.public_key)
                            .observe(this@MainActivityKt, { token ->
                                intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token)
                                autoLoginWithToken()
                            })
                })
            }
        }
    }

    private fun startLocation() {
        mClient.setLocationListener(this@MainActivityKt)
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mOption.isOnceLocation = true
        mOption.isLocationCacheEnable = true
        mClient.setLocationOption(mOption)
        mClient.startLocation()
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
    interface OnLoginStateChangedListener {
        fun onLoginStateChanged()
    }

    fun setOnLoginActivityListener(listener: OnLoginStateChangedListener) {
        mOnLoginStateChangedListener = listener
    }

    interface OnLocationListener {
        fun onLocated(aMapLocation: AMapLocation?)
        fun onPermissionDenied()
    }

    fun setOnLocationListener(listener: OnLocationListener) {
        mOnLocationListener = listener
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragment(mHome)
                return true
            }
            R.id.navigation_my -> {
                showFragment(mMy)
                return true
            }
            R.id.navigation_box -> {
                showFragment(mPurchasing)
                return true
            }
        }
        return false
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        mOnLocationListener?.onLocated(aMapLocation)
        intent.putExtra(ConstantUtil.LONGITUDE, aMapLocation?.longitude)
        intent.putExtra(ConstantUtil.LATITUDE, aMapLocation?.latitude)
    }

    /**
     * 帮助AndroidAutoSize适配屏幕
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }
}
package com.example.schoolairdroprefactoredition.scene.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewItemActivity
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPlaygroundFragment
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPurchasingFragment
import com.example.schoolairdroprefactoredition.scene.main.messages.MessagesFragment
import com.example.schoolairdroprefactoredition.scene.main.my.MyFragment
import com.example.schoolairdroprefactoredition.scene.search.SearchFragment
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PermissionBaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        AMapLocationListener {

    companion object {
        private var autoLogged = false

        /**
         * 通知MainActivity主题改变
         */
        fun notifyThemeChanged() {
            autoLogged = false
            BaseChildFragment.notifyThemeChanged()
        }
    }

    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val accountViewModel by lazy {
        ViewModelProvider(this).get(AccountViewModel::class.java)
    }

    private val mClient by lazy {
        AMapLocationClient(this@MainActivity)
    }

    private val mOption by lazy {
        AMapLocationClientOption()
    }

    private val mSearch by lazy {
        SearchFragment.newInstance()
    }

    private val mPlayground by lazy {
        ParentPlaygroundFragment.newInstance()
    }

    private val mMessages by lazy {
        MessagesFragment()
    }

    private val mMy by lazy {
        MyFragment.newInstance()
    }

    private val mPurchasing by lazy {
        ParentPurchasingFragment.newInstance()
    }

    private var mOnLoginStateChangedListener: ArrayList<OnLoginStateChangedListener> = ArrayList()
    private var mOnLocationListenerList: ArrayList<OnLocationListener> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAgreeToTermsOfServiceAndPrivacyPolicy()
    }

    /**
     * 当app可见时检查当前页面与导航栏指向页面一致
     */
    override fun onResume() {
        super.onResume()
        when {
            mPurchasing.isVisible -> navView.selectedItemId = R.id.navigation_purchasing
            mPlayground.isVisible -> navView.selectedItemId = R.id.navigation_playground
            mMessages.isVisible -> navView.selectedItemId = R.id.navigation_message
            mMy.isVisible -> navView.selectedItemId = R.id.navigation_my
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                LoginActivity.LOGIN -> {
                    if (data == null) {
                        intent.removeExtra(ConstantUtil.KEY_USER_INFO)
                        intent.removeExtra(ConstantUtil.KEY_AUTHORIZE)
                    } else {
                        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, data.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE))
                        intent.putExtra(ConstantUtil.KEY_USER_INFO, data.getSerializableExtra(ConstantUtil.KEY_USER_INFO))
                    }

                    for (listener in mOnLoginStateChangedListener) {
                        listener.onLoginStateChanged()
                    }
                }

                UserActivity.REQUEST_UPDATE -> {
                    if (data?.getBooleanExtra(ConstantUtil.KEY_UPDATED, false) as Boolean) {
                        autoLoginWithToken()
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mClient.stopLocation()
        mClient.unRegisterLocationListener(this@MainActivity)

        mOnLocationListenerList.clear()
        mOnLoginStateChangedListener.clear()
    }

    private fun initView() {
        mPlayground.setOnSearchBarClickedListener {
            showSearch()
        }

        mPurchasing.setOnSearchBarClickedListener {
            showSearch()
        }

        home_add_fab.setOnClickListener {
            AddNewItemActivity.start(this, intent.extras)
        }

        navView.setOnNavigationItemSelectedListener(this@MainActivity)
        navView.selectedItemId = R.id.navigation_purchasing
    }

    /**
     * 当用户同意使用服务条款时才加载首页内容
     */
    override fun agreeToTermsOfService() {
        super.agreeToTermsOfService()
        notifyThemeChanged()
        setContentView(R.layout.activity_main)

        initCache()
        initView()
    }

    override fun locationGranted() {
        super.locationGranted()
        startLocation()
    }

    override fun locationDenied() {
        super.locationDenied()
        for (onLocationListener in mOnLocationListenerList) {
            onLocationListener.onPermissionDenied()
        }
    }

    private fun initCache() {
        accountViewModel.lastLoggedTokenCaChe.observe(this@MainActivity, {
            intent.putExtra(ConstantUtil.KEY_AUTHORIZE, it)
        })
        accountViewModel.lastLoggedUserInfoCache.observe(this@MainActivity, {
            intent.putExtra(ConstantUtil.KEY_USER_INFO, it)
        })
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            if (fragment.isHidden || !fragment.isAdded) {
                beginTransaction()
                        .hide(mPlayground)
                        .hide(mMy)
                        .hide(mPurchasing)
                        .hide(mMessages)
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

    private fun showSearch() {
        supportFragmentManager.apply {
            if (mSearch.isHidden || !mSearch.isAdded) {
                if (!mSearch.isAdded)
                    beginTransaction()
                            .add(R.id.container, mSearch)
                            .addToBackStack(null)
                            .show(mSearch)
                            .commit()
                else
                    beginTransaction()
                            .show(mSearch)
                            .addToBackStack(null)
                            .commit()
            }
        }
    }

    /**
     * 在有本地用户token时自动登录
     * 如果获取本地token为空而用户基本信息仍有缓存则代表用户token已过期，将自动重新登录
     * <p>
     * token 有效时间在LoginImpl中修改，否则与服务器过期时间不同会导致用户自动登录失败
     *
     * 同步锁防止多个子fragment同时请求MainActivity的登陆
     */
    @Synchronized
    fun autoLogin() {
        if (!autoLogged) {
            autoLogged = true // 已自动登录标识，防止多个子fragment调用此方法
            accountViewModel.lastLoggedTokenCaChe.observe(this, {
                if (it != null) { // token 仍有效 使用本地缓存重新获取token后登录
                    intent.putExtra(ConstantUtil.KEY_AUTHORIZE, it)
                    autoLoginWithToken()
                } else {
                    accountViewModel.lastLoggedUserInfoCache.observe(this, { infoCache ->
                        // token 已无效 使用本地缓存重新获取token后登录
                        if (infoCache != null) {
                            intent.putExtra(ConstantUtil.KEY_USER_INFO, infoCache)
                            autoReLoginWithCache()
                        }
                    })
                }
            })
        }
    }

    /**
     * 用当前获取的公钥加密alipay并登录
     * 在以下情形调用
     * 1、有缓存情况下打开app时
     * 2、用户信息修改后回到MainActivity
     */
    private fun autoLoginWithToken() {
        val token: DomainAuthorize = intent.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE) as DomainAuthorize
        if (token.access_token != null) {
            loginViewModel.getUserInfo(token.access_token).observe(this, {

                Toast.makeText(this@MainActivity, "登录成功", Toast.LENGTH_SHORT).show()

                val userInfo = it?.data?.get(0)
                intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
                for (listener in mOnLoginStateChangedListener) {
                    listener.onLoginStateChanged()
                }
            })
        }
    }

    /**
     * 使用已有的缓存进行登录
     * 在app首次打开时调用
     */
    private fun autoReLoginWithCache() {
        val userInfo: DomainUserInfo.DataBean = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean
        var gettingPublicKey = false
        if (userInfo.ualipay != null) {
            loginViewModel.getPublicKey().observe(this@MainActivity, { publicK ->
                if (!gettingPublicKey) {
                    gettingPublicKey = true
                    authorizeWithAlipayID(userInfo, publicK)
                }
            })
        }
    }

    /**
     * 使用同步锁定防止多次同时请求
     */
    @Synchronized
    private fun authorizeWithAlipayID(userInfo: DomainUserInfo.DataBean, publicK: DomainAuthorizeGet) {
        loginViewModel.authorizeWithAlipayID(
                publicK.cookie,
                userInfo.ualipay,
                publicK.public_key)
                .observe(this@MainActivity, { token ->
                    intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token)
                    autoLoginWithToken()
                })
    }

    private fun startLocation() {
        mClient.setLocationListener(this@MainActivity)
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

    fun addOnLoginActivityListener(listener: OnLoginStateChangedListener) {
        mOnLoginStateChangedListener.add(listener)
    }

    interface OnLocationListener {
        fun onLocated(aMapLocation: AMapLocation?)
        fun onPermissionDenied()
    }

    fun addOnLocationListener(listener: OnLocationListener) {
        mOnLocationListenerList.add(listener)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_purchasing -> {
                showFragment(mPurchasing)
                return true
            }
            R.id.navigation_playground -> {
                showFragment(mPlayground)
                return true
            }
            R.id.navigation_message -> {
                showFragment(mMessages)
                return true
            }
            R.id.navigation_my -> {
                showFragment(mMy)
                return true
            }
        }
        return false
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        for (onLocationListener in mOnLocationListenerList) {
            onLocationListener.onLocated(aMapLocation)
        }
        intent.putExtra(ConstantUtil.LONGITUDE, aMapLocation?.longitude)
        intent.putExtra(ConstantUtil.LATITUDE, aMapLocation?.latitude)
    }
}
package com.example.schoolairdroprefactoredition.scene.main

import android.app.Activity
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPlaygroundFragment
import com.example.schoolairdroprefactoredition.scene.main.home.ParentPurchasingFragment
import com.example.schoolairdroprefactoredition.scene.main.messages.MessagesFragment
import com.example.schoolairdroprefactoredition.scene.main.my.MyFragment
import com.example.schoolairdroprefactoredition.scene.search.SearchFragment
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.AccountViewModel
import com.example.schoolairdroprefactoredition.viewmodel.InstanceMessageViewModel
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : PermissionBaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        AMapLocationListener, Application.IMListener {

    companion object {
        /**
         * app打开时是否已有子fragment请求过自动登录
         * 若无则自动登录，防止一次打开多次登录
         */
        private var autoLogged = false

        /**
         * 通知MainActivity主题改变
         * 重置某些页面自动加载数据的标志位
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

    private val imViewModel by lazy {
        InstanceMessageViewModel.InstanceViewModelFactory((application as Application).chatRepository).create(InstanceMessageViewModel::class.java)
    }

    private val mClient by lazy {
        AMapLocationClient(this@MainActivity)
    }

    private val mOption by lazy {
        AMapLocationClientOption()
    }

    /**
     * 搜索 页面
     */
    private val mSearch by lazy {
        SearchFragment.newInstance()
    }

    /**
     * 广场 页面
     */
    private val mPlaza by lazy {
        ParentPlaygroundFragment.newInstance()
    }

    /**
     * 消息 页面
     */
    private val mMessages = MessagesFragment()

    /**
     * 我的 页面
     */
    private val mMy by lazy {
        MyFragment.newInstance()
    }

    /**
     * 淘物 页面
     */
    private val mPurchasing by lazy {
        ParentPurchasingFragment.newInstance()
    }

    /**
     * app登录状态改变监听器
     */
    private var mOnLoginStateChangedListeners: ArrayList<OnLoginStateChangedListener> = ArrayList()

    /**
     * 高德定位状态改变监听器
     */
    private var mOnLocationListeners: ArrayList<OnLocationListener> = ArrayList()

    /**
     * 本页面初始化前询问用户是否同意服务协议
     *
     * 同意后将页面初始化代码写在
     * [MainActivity.initAppMainAfterAgreeToTermsOfService]
     * 不同意则将退出App
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAgreeToTermsOfServiceAndPrivacyPolicy(this@MainActivity)
    }


    /**
     * 加载[MainActivity]页面内容
     *
     * 父类调用本方法的前提时必须先同意
     * [MainActivity.checkIfAgreeToTermsOfServiceAndPrivacyPolicy]
     */
    override fun initAppMainAfterAgreeToTermsOfService() {
        super.initAppMainAfterAgreeToTermsOfService()
        notifyThemeChanged()
        setContentView(R.layout.activity_main)
        initCache()
        initListener()
//        setMainActivitySaturation0()
    }

    /**
     * 当app可见时检查当前页面与导航栏指向页面一致
     */
    override fun onResume() {
        super.onResume()
        when {
            mPurchasing.isVisible -> {
                if (navView.selectedItemId != R.id.navigation_purchasing) {
                    navView.selectedItemId = R.id.navigation_purchasing
                }
            }
            mPlaza.isVisible -> {
                if (navView.selectedItemId != R.id.navigation_playground) {
                    navView.selectedItemId = R.id.navigation_playground
                }
            }
            mMessages.isVisible -> {
                if (navView.selectedItemId != R.id.navigation_message) {
                    navView.selectedItemId = R.id.navigation_message
                }
            }
            mMy.isVisible -> {
                navView.selectedItemId = R.id.navigation_my
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                LoginActivity.LOGIN -> {
                    val userInfo = data?.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean
                    val token = data?.getSerializableExtra(ConstantUtil.KEY_TOKEN) as? DomainToken

                    if (data == null) {
                        intent.removeExtra(ConstantUtil.KEY_USER_INFO)
                        intent.removeExtra(ConstantUtil.KEY_TOKEN)
                    } else {
                        intent.putExtra(ConstantUtil.KEY_TOKEN, token)
                        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
                    }

                    loginStateChanged(userInfo, token)
                }

                UserActivity.REQUEST_UPDATE -> {
                    if (data?.getBooleanExtra(ConstantUtil.KEY_UPDATED, false) as Boolean) {
                        autoLoginWithToken()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mClient.stopLocation()
        mClient.unRegisterLocationListener(this@MainActivity)

        mOnLocationListeners.clear()
        mOnLoginStateChangedListeners.clear()
    }

    private fun initListener() {
        (application as Application).addOnIMListener(this@MainActivity)

        mPlaza.setOnSearchBarClickedListener {
            showSearch()
        }

        mPurchasing.setOnSearchBarClickedListener {
            showSearch()
        }

        home_add_fab.setOnClickListener {
            AddNewActivity.start(this, AddNewActivity.AddNewType.ADD_POST)
        }

        navView.setOnNavigationItemSelectedListener(this@MainActivity)
        navView.selectedItemId = R.id.navigation_purchasing
    }

    /**
     * 将本页面颜色饱和度变为0
     *
     * 清明等祭祀节日主题
     */
    private fun setMainActivitySaturation0() {
        val colorMatrix = ColorMatrix()
        val paint = Paint()
        val contentContainer = window.decorView

        colorMatrix.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
    }

    /*
     * 父类接收到的定位权限被允许，在此进行定位操作
     */
    override fun locationGranted() {
        super.locationGranted()
        startLocation()
    }

    /**
     * 父类接收到的定位权限被拒绝
     */
    override fun locationDenied() {
        super.locationDenied()
        for (onLocationListener in mOnLocationListeners) {
            onLocationListener.onLocationPermissionDenied()
        }
    }

    /**
     * 读取手机缓存和账号设置
     */
    private fun initCache() {
        val tokenCache = accountViewModel.lastLoggedTokenCaChe.value
        val userInfoCache = accountViewModel.lastLoggedUserInfoCache.value
        tokenCache?.let {
            intent.putExtra(ConstantUtil.KEY_TOKEN, it)
        }
        userInfoCache?.let {
            intent.putExtra(ConstantUtil.KEY_USER_INFO, it)
        }

    }

    /**
     * 切换子页面的显示
     * 当页面未被添加或者未显示时先先添加及显示
     * 当已经显示时再点击导航栏图标将滑动至最顶部
     *
     * 注意：
     * 这里页面显示时再点击使得页面滑动至顶部的逻辑不能用
     * [BottomNavigationView.setOnNavigationItemReselectedListener]
     * 这个接口实现，这个接口的用意是让图标在第二次被点击
     * 的时候才能被选中，这样第一次进app的时候第一个页面就
     * 会一直是虚空状态，要先切换到其他页面再切回来才能正常
     */
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            if (fragment.isHidden || !fragment.isAdded) {
                beginTransaction()
                        .hide(mPlaza)
                        .hide(mMy)
                        .hide(mPurchasing)
                        .hide(mMessages)
                        .commit()

                if (!fragment.isAdded) {
                    beginTransaction()
                            .add(R.id.navHostFragment, fragment)
                            .show(fragment)
                            .commit()
                } else {
                    beginTransaction()
                            .show(fragment)
                            .commit()
                }
            } else if (fragment.isAdded) {
                when (fragment) {
                    mPurchasing -> {
                        mPurchasing.pageScrollToTop()
                    }
                    mPlaza -> {
                        mPlaza.pageScrollToTop()
                    }
                    mMessages -> {
                        mMessages.pageScrollToTop()
                    }
                }
            }
        }
    }

    /**
     * 显示搜索页面
     */
    private fun showSearch() {
        supportFragmentManager.apply {
            if (mSearch.isHidden || !mSearch.isAdded) {
                if (!mSearch.isAdded) {
                    beginTransaction()
                            .add(R.id.container, mSearch)
                            .addToBackStack(null)
                            .show(mSearch)
                            .commit()
                } else {
                    beginTransaction()
                            .show(mSearch)
                            .addToBackStack(null)
                            .commit()
                }
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
            val token = accountViewModel.lastLoggedTokenCaChe.value
            if (token != null) { // token 仍有效 使用本地缓存重新获取token后登录
                intent.putExtra(ConstantUtil.KEY_TOKEN, token)
                autoLoginWithToken()
            } else {
                // token 已无效 使用本地缓存重新获取token后登录
                val infoCache = accountViewModel.lastLoggedUserInfoCache.value
                if (infoCache != null) {
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, infoCache)
                    autoReLoginWithCache()
                }
            }
        }
    }

    /**
     * 用当前获取的公钥加密alipay并登录
     * 在以下情形调用
     * 1、有缓存情况下打开app时
     * 2、用户信息修改后回到MainActivity
     */
    private fun autoLoginWithToken() {
        for (listener in mOnLoginStateChangedListeners) {
            listener.onLogging()
        }

        val token: DomainToken = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken
        if (token.access_token != null) {
            loginViewModel.getUserInfo(token.access_token).observe(this, {
                intent.putExtra(ConstantUtil.KEY_USER_INFO, it)

                loginStateChanged(it, token)
            })
        }
    }

    /**
     * 使用已有的缓存进行登录
     * 在app首次打开时调用
     */
    private fun autoReLoginWithCache() {
//        val userInfo: DomainUserInfo.DataBean = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean
        var gettingPublicKey = false
        loginViewModel.getPublicKey().observe(this@MainActivity, { publicK ->
            if (!gettingPublicKey) {
                gettingPublicKey = true
                authorizeWithAlipayID(publicK)
            }
        })
    }

    /**
     * 使用alipay id登录
     * 注解为同步方法以防止多次同时请求
     */
    @Synchronized
    private fun authorizeWithAlipayID(publicK: DomainAuthorizeGet) {
        loginViewModel.authorizeWithAlipayID(
                publicK.cookie,
                AppConfig.USER_ALIPAY,
                publicK.public_key)
                .observe(this@MainActivity, { token ->
                    intent.putExtra(ConstantUtil.KEY_TOKEN, token)
                    autoLoginWithToken()
                })
    }

    /**
     * 开始定位
     * 结果回调在[MainActivity.onLocationChanged]中
     */
    private fun startLocation() {
        mClient.setLocationListener(this@MainActivity)
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mOption.isOnceLocation = true
        mOption.isLocationCacheEnable = true
        mClient.setLocationOption(mOption)
        mClient.startLocation()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_purchasing -> {
                showFragment(mPurchasing)
                return true
            }
            R.id.navigation_playground -> {
                showFragment(mPlaza)
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

    /**
     * 高德定位SDK结果回调
     * 开始定位的方法在[MainActivity.startLocation]
     */
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        for (onLocationListener in mOnLocationListeners) {
            onLocationListener.onLocated(aMapLocation)
        }
        intent.putExtra(ConstantUtil.LONGITUDE, aMapLocation?.longitude)
        intent.putExtra(ConstantUtil.LATITUDE, aMapLocation?.latitude)
    }


    /*************************************************************************************/
    /**************************************listeners**************************************/
    /*************************************************************************************/
    /**
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
        /**
         *  登录状态改变的回调
         *  使用[MainActivity.loginStateChanged]来通知监听器
         *
         *  @param intent
         *  包含的键值对
         *  [ConstantUtil.KEY_TOKEN] token 类型为[DomainToken]
         *  [ConstantUtil.KEY_USER_INFO] 我的用户信息 类型为[DomainUserInfo.DataBean]
         */
        fun onLoginStateChanged(intent: Intent)

        /**
         * 正在登录 以及 登录结束 的回调
         * @param isLogging
         * true 正在登录
         * false 登录结束
         */
        fun onLogging()
    }

    /**
     * 添加接收登录结果回调的监听器
     *
     * 添加的监听器来自需要登录状态和账号信息的子页面
     * 以便在登陆状态改变时即时通知子页面ui更新
     */
    fun addOnLoginActivityListener(listener: OnLoginStateChangedListener) {
        mOnLoginStateChangedListeners.add(listener)
    }

    /**
     * 为监听登录状态改变的监听器回调登录状态
     *
     * 并且根据app登录状态来改变IM系统的登录状态
     */
    private fun loginStateChanged(userInfo: DomainUserInfo.DataBean?, token: DomainToken?) {
        (application as Application).cacheMyInfoAndToken(userInfo, token)

        for (listener in mOnLoginStateChangedListeners) {
            listener.onLoginStateChanged(intent)
        }

        // 若为登录app回调，则登录IM，否则退出IM
        if (userInfo != null && token != null) {
            (application as Application).doLoginIM()
        } else {
            (application as Application).doLogoutIM()
        }
    }

    /**
     * 定位回调接口
     * 获取结果 或 处理权限被拒绝时
     */
    interface OnLocationListener {
        /**
         * 定位结果回调
         */
        fun onLocated(aMapLocation: AMapLocation?)

        /**
         * 定位权限被拒绝
         */
        fun onLocationPermissionDenied()
    }

    /**
     * 添加接收定位回调的监听器
     *
     * 来自需要位置信息的子页面
     * 以便在获取位置信息后即时更新页面ui
     */
    fun addOnLocationListener(listener: OnLocationListener) {
        mOnLocationListeners.add(listener)
    }


    /*****************************************************************************************/
    /************************************** IM listeners**************************************/
    /*****************************************************************************************/
    override fun onIMStartLogin() {
        // do nothing
    }

    override fun onIMLoginResponse(code: Int) {
        // do nothing
    }

    override fun onIMLinkDisconnect(code: Int) {
        // do nothing
    }

    override fun onIMMessageLost(lostMessages: ArrayList<Protocal>) {
        // do nothing
    }

    override fun onIMMessageBeReceived(fingerprint: String) {
        // do nothing
    }

    override fun onIMReceiveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        // 保存收到的消息
        val myID = (application as Application).getCachedMyInfo()
        if (myID != null) {
            imViewModel.saveReceivedMessage(ChatHistory(fingerprint, senderID, myID.userId.toString(), typeu, content, Date(), 0))
        }

    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
        // do nothing
    }

}
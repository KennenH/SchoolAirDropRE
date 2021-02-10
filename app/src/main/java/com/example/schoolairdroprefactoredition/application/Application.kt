package com.example.schoolairdroprefactoredition.application

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.database.SARoomDatabase
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.im.IMClientManager
import com.example.schoolairdroprefactoredition.repository.ChatAllRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.MiPushClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import net.x52im.mobileimsdk.android.core.LocalDataSender
import net.x52im.mobileimsdk.android.event.ChatBaseEvent
import net.x52im.mobileimsdk.android.event.ChatMessageEvent
import net.x52im.mobileimsdk.android.event.MessageQoSEvent
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class Application : Application(), ChatBaseEvent, MessageQoSEvent, ChatMessageEvent {

    companion object {
        open class LogoutAsync() : AsyncTask<Any?, Int?, Int?>() {
            private var context: WeakReference<Context>? = null

            constructor(context: Context) : this() {
                this.context = WeakReference<Context>(context)
            }

            override fun doInBackground(vararg params: Any?): Int {
                var code = -1
                try {
                    code = LocalDataSender.getInstance().sendLoginout()
                } catch (e: Exception) {
                }

                IMClientManager.getInstance(context?.get()).reflagInit()
                return code
            }
        }
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val schoolAirdropDatabase by lazy {
        SARoomDatabase.getDatabase(this, applicationScope)
    }

    val chatRepository by lazy {
        ChatAllRepository(schoolAirdropDatabase.chatHistoryDao())
    }

    private val chatClientManager by lazy {
        IMClientManager.getInstance(this)
    }

    /**
     * IM事件回调的监听者们
     */
    private val eventIMListeners: ArrayList<IMListener> = ArrayList()

    /**
     * app登录回调监听者们
     *
     * 不同于[com.example.schoolairdroprefactoredition.scene.main.MainActivity]中的登录监听器的是
     * 该回调用于和主页面距离比较远的，或者不由主页面管理的Activity页面
     */
    private val applicationLoginStateListeners: ArrayList<OnApplicationLoginListener> = ArrayList()

    /**
     * 我的用户基本信息
     */
    private var cachedMyInfo: DomainUserInfo.DataBean? = null

    /**
     * 我的token
     */
    private var cachedToken: DomainToken? = null

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        initAdapt()
        initMiPush()
        initAppTheme()
        initIM()
    }

    /**
     * 初始化聊天系统
     */
    private fun initIM() {
        chatClientManager.initMobileIMSDK()
    }

    /**
     * 设置App主题
     * 默认为白色，且不跟随系统
     */
    private fun initAppTheme() {
        val mJsonCacheUtil = JsonCacheUtil.newInstance()
        var settings = mJsonCacheUtil.getValue(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache(false)
        AppCompatDelegate.setDefaultNightMode(if (settings.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setAppTheme(isDarkTheme: Boolean) {
        val mJsonCacheUtil = JsonCacheUtil.newInstance()
        var settings = mJsonCacheUtil.getValue(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache(false)
        AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        settings.isDarkTheme = isDarkTheme
        mJsonCacheUtil.saveCache(UserSettingsCache.KEY, settings)
    }

    private fun initAdapt() {
        AutoSize.initCompatMultiProcess(this)
        val config = AutoSizeConfig.getInstance()
                .setLog(true)
                .setDesignWidthInDp(360)
                .setDesignHeightInDp(640)
                .setUseDeviceSize(true)
        val screenWidth = config.screenWidth.toFloat()
        val screenHeight = config.screenHeight.toFloat()
        val designWidthInDp = config.designWidthInDp.toFloat()
        val designHeightInDp = config.designHeightInDp.toFloat()
        val rateWidth = screenWidth / designWidthInDp
        val rateHeight = screenHeight / designHeightInDp
        config.isBaseOnWidth = rateWidth <= rateHeight
    }

    private fun initMiPush() {
        if (shouldInitMiPush()) {
            MiPushClient.registerPush(this, ConstantUtil.MIPUSH_APP_ID, ConstantUtil.MIPUSH_APP_KEY)
        }
        val newLogger: LoggerInterface = object : LoggerInterface {
            override fun setTag(tag: String) {
                // ignore
            }

            override fun log(content: String, t: Throwable) {
                LogUtils.d(content, t)
            }

            override fun log(content: String) {
                LogUtils.d(content)
            }
        }
        //        Logger.setLogger(this, newLogger);
    }

    private fun shouldInitMiPush(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        val mainProcessName = applicationInfo.processName
        val myPid = Process.myPid()
        for (info in processInfos) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    /**
     * 在app中保存我的用户信息以及token
     * 当token或者用户信息单独保存时不会将另一个置空
     * 只有当两参都为空时才会将两者同时置空
     */
    fun cacheMyInfoAndToken(userInfo: DomainUserInfo.DataBean? = null, token: DomainToken? = null) {
        if (userInfo != null && token != null) {
            cachedMyInfo = userInfo
            cachedToken = token
        } else if (userInfo != null) {
            cachedMyInfo = userInfo
        } else if (token != null) {
            cachedToken = token
        } else {
            cachedMyInfo = null
            cachedToken = null
        }
    }

    /**
     * 获取token
     */
    fun getCachedToken(): DomainToken? {
        return cachedToken
    }

    /**
     * 获取用户信息
     */
    fun getCachedMyInfo(): DomainUserInfo.DataBean? {
        return cachedMyInfo
    }

    /*****************************************************************************/
    /***************************** 监听器和回调方法 *******************************/
    /*****************************************************************************/

    /**
     * app登录状态回调
     */
    interface OnApplicationLoginListener {
        /**
         * app登录后回调
         */
        fun onApplicationLoginStateChange(isLogged: Boolean)
    }

    fun addOnApplicationLoginListener(listener: OnApplicationLoginListener) {
        applicationLoginStateListeners.add(listener)
    }

    /**
     * 注册IM系统事件监听回调
     */
    fun addOnIMListener(imListener: IMListener) {
        eventIMListeners.add(imListener)
    }

    /**
     * 登录IM系统
     */
    fun doLoginIM() {
        // IM正在登录的回调
        for (listener in eventIMListeners) {
            listener.onIMStartLogin()
        }

        // app登录状态改变回调
        for (listener in applicationLoginStateListeners) {
            listener.onApplicationLoginStateChange(true)
        }

        // 发送登录包
        object : LocalDataSender.SendLoginDataAsync(cachedMyInfo?.userId.toString(), cachedToken?.access_token?.substring(7)) {}.execute()
    }

    /**
     * 退出IM系统
     */
    fun doLogoutIM() {
        // app登录状态改变回调
        for (listener in applicationLoginStateListeners) {
            listener.onApplicationLoginStateChange(false)
        }

        // 发送退出登录包
        object : LogoutAsync(this@Application) {}.execute()
    }

    /**
     * IM系统事件监听器
     */
    interface IMListener {
        /**
         * 开始登录
         */
        fun onIMStartLogin()

        /**
         * 登录结果回调
         * 0为登录成功
         */
        fun onIMLoginResponse(code: Int)

        /**
         * 连接断开回调
         * 需要显示连接已断开
         */
        fun onIMLinkDisconnect(code: Int)

        /**
         * 发送的在线消息未发送成功
         * 需要显示在消息旁以提示用户发送失败
         */
        fun onIMMessageLost(lostMessages: ArrayList<Protocal>)

        /**
         * 在线消息已被接收或已离线存储
         */
        fun onIMMessageBeReceived(fingerprint: String)

        /**
         * 收到来自sender的消息
         * 保存至数据库并显示在消息列表中
         */
        fun onIMReceiveMessage(fingerprint: String, senderID: String, content: String, typeu: Int)

        /**
         * 收到服务端发送的错误回调
         * 可能是服务器系统出错，此时可能需要通知用户系统崩溃，不能发送消息
         */
        fun onIMErrorResponse(errorCode: Int, message: String)
    }

    override fun onLoginResponse(code: Int) {
        for (listener in eventIMListeners) {
            listener.onIMLoginResponse(code)
        }
    }

    override fun onLinkClose(code: Int) {
        for (listener in eventIMListeners) {
            listener.onIMLinkDisconnect(code)
        }
    }

    override fun messagesLost(lostMessages: ArrayList<Protocal>) {
        for (listener in eventIMListeners) {
            listener.onIMMessageLost(lostMessages)
        }
    }

    override fun messagesBeReceived(fingerprint: String) {
        for (listener in eventIMListeners) {
            listener.onIMMessageBeReceived(fingerprint)
        }
    }

    override fun onRecieveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        for (listener in eventIMListeners) {
            listener.onIMReceiveMessage(fingerprint, senderID, content, typeu)
        }
    }

    override fun onErrorResponse(errorCode: Int, message: String) {
        for (listener in eventIMListeners) {
            listener.onIMErrorResponse(errorCode, message)
        }
    }
}
package com.example.schoolairdroprefactoredition.application

import android.app.ActivityManager
import android.app.Application
import android.os.AsyncTask
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.database.SARoomDatabase
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.im.IMClientManager
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.example.schoolairdroprefactoredition.viewmodel.ChatViewModel
import com.example.schoolairdroprefactoredition.viewmodel.InstanceMessageViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.lzf.easyfloat.EasyFloat
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.MiPushClient
import kotlinx.android.synthetic.main.activity_main.*
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
import kotlin.collections.ArrayList

class SAApplication : Application(), ChatBaseEvent, MessageQoSEvent, ChatMessageEvent, LifecycleObserver {

    companion object {
        open class LogoutAsync() : AsyncTask<Any?, Int?, Int?>() {
            private var application: Application? = null

            constructor(application: Application) : this() {
                this.application = application
            }

            override fun doInBackground(vararg params: Any?): Int {
                var code = -1
                try {
                    code = LocalDataSender.getInstance().sendLoginout()
                } catch (e: Exception) {
                }

                // 重置初始化标志符，否则会报203
                IMClientManager.getInstance(application).resetInit()
                return code
            }
        }
    }

    /**
     * 发送文本消息
     *
     * application作为app顶层类生命周期最长，只要application不被销毁也意味着app没有被杀死
     */
    fun sendTextMessage(userID: String, myID: String, content: String, weakAdapter: WeakReference<ChatRecyclerAdapter>) {
        // 为本条消息创建消息指纹
        val fingerprint = Protocal.genFingerPrint()
        // 为新发送的消息new一个对象
        val chat = ChatHistory(fingerprint, myID, userID, ConstantUtil.MESSAGE_TYPE_TEXT, content, System.currentTimeMillis(), ChatRecyclerAdapter.MessageSendStatus.SENDING)
        // 获取adapter和recycler view的弱引用
        val adapter = weakAdapter.get()
        // 将发送的消息显示到消息框中
        adapter?.addData(0, chat)
        // 保存自己发送的消息
        chatViewModel.saveSentMessage(chat)
        // 不能给自己发送消息
        if (userID != myID) {
            // 框架异步发送消息
            object : LocalDataSender.SendCommonDataAsync(content, userID, fingerprint, ConstantUtil.MESSAGE_TYPE_TEXT) {
                override fun onPostExecute(code: Int?) {
                    if (code != 0) chatViewModel.updateMessageStatus(fingerprint, ChatRecyclerAdapter.MessageSendStatus.FAILED)
                }
            }.execute()
        }
    }

    /**
     * 发送图片消息 多图
     */
    fun sendImageMessage(userID: String, myID: String, imagePaths: List<String>, weakAdapter: WeakReference<ChatRecyclerAdapter>) {
        val adapter = weakAdapter.get()
        // 先暂时保存这几个new出来的chat对象，后面图片发送完毕之后发送消息还要使用
        val chatList = ArrayList<ChatHistory>(imagePaths.size)
        for (path in imagePaths) {
            val fingerprint = Protocal.genFingerPrint()
            val chat = ChatHistory(fingerprint, myID, userID, ConstantUtil.MESSAGE_TYPE_IMAGE, path, System.currentTimeMillis(), ChatRecyclerAdapter.MessageSendStatus.SENDING)
            chatList.add(chat)
            adapter?.addData(0, chat)
            chatViewModel.saveSentMessage(chat)
        }

        // 不能给自己发消息
        if (userID != myID) {
            chatViewModel.sendImageMessage(getCachedToken()?.access_token, imagePaths).observeOnce {
                if (it != null) {
                    for ((index, path) in it.withIndex()) {
                        object : LocalDataSender.SendCommonDataAsync(path, userID, chatList[index]?.fingerprint, ConstantUtil.MESSAGE_TYPE_IMAGE) {
                            override fun onPostExecute(code: Int?) {
                                if (code != 0) chatViewModel.updateMessageStatus(chatList[index]?.fingerprint, ChatRecyclerAdapter.MessageSendStatus.FAILED)
                            }
                        }.execute()
                    }
                } else {
                    for (history in chatList) {
                        chatViewModel.updateMessageStatus(history.fingerprint, ChatRecyclerAdapter.MessageSendStatus.FAILED)
                    }
                }
            }
        }
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val schoolAirdropDatabase by lazy {
        SARoomDatabase.getDatabase(this, applicationScope)
    }

    val databaseRepository by lazy {
        DatabaseRepository(schoolAirdropDatabase.databaseDao())
    }

    private val chatClientManager by lazy {
        IMClientManager.getInstance(this)
    }

    private val chatViewModel by lazy {
        ChatViewModel.ChatViewModelFactory(databaseRepository).create(ChatViewModel::class.java)
    }

    private val imViewModel by lazy {
        InstanceMessageViewModel.InstanceViewModelFactory(databaseRepository).create(InstanceMessageViewModel::class.java)
    }

    /**
     * 监听app进入前后台的监听者们
     */
    private val appRuntimeStatusListeners: ArrayList<OnAppStatusChangeListener> = ArrayList()

    /**
     * 是否需要在app进入前台的时候调用连接接口
     *
     * 1、第一次打开app时无需调用，之后每次进入后台时将它置为true
     * 2、每次connect调用分为两个部分，第一部分为验证token是否过期，第二部分为拉取离线消息
     * 3、验证token的调用有10秒钟的间隔
     * 4、app进入前台时先检查im框架回调，若im在后台期间不曾断开连接，则无需拉取离线消息
     */
    private var isNeedVerifyTokenWhenComesToForeground = false

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
     * 当该监听器回调时需要主页面在消息列表的图标上显示小红点
     */
    private var shouldShowBadgeListener: OnShouldShowBadgeListener? = null

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
        initInAppFloat()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this) // 给予application感知app进入前后台的能力
    }

    private fun initInAppFloat() {
        EasyFloat.init(this, AppConfig.IS_DEBUG) // 浮窗初始化
    }

    /**
     * 设置App主题
     * 默认为白色，且不跟随系统
     */
    private fun initAppTheme() {
        val mJsonCacheUtil = JsonCacheUtil.getInstance()
        var settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache()
        AppCompatDelegate.setDefaultNightMode(if (settings.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setAppTheme(isDarkTheme: Boolean) {
        val mJsonCacheUtil = JsonCacheUtil.getInstance()
        var settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache()
        AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        settings.isDarkTheme = isDarkTheme
        mJsonCacheUtil.saveCache(UserSettingsCache.KEY, settings)
    }

    private fun initAdapt() {
        AutoSize.initCompatMultiProcess(this)
        val config = AutoSizeConfig.getInstance()
                .setLog(false)
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
     * app 进入前台回调，打开app时将不会被回调
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResume() {
        // 打开app时将不会回调
        if (isNeedVerifyTokenWhenComesToForeground) {
            // 登录im系统
            doLoginIM()
            for (listener in appRuntimeStatusListeners) {
                listener.onAppEnterForeground()
            }
        }
    }

    /**
     * app 进入后台回调
     *
     * 将[isNeedVerifyTokenWhenComesToForeground]置为true，下次回到前台调用验证token
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPause() {
        // 下次app进入前台时将会执行验证token和登录im操作
        isNeedVerifyTokenWhenComesToForeground = true
        // 退出im系统，开始接收im离线消息
        doLogoutIM()
        for (listener in appRuntimeStatusListeners) {
            listener.onAppEnterBackground()
        }
    }

    /**
     * 添加一个app运行状态的监听器
     */
    fun addOnAppStatusChangeListener(listener: OnAppStatusChangeListener) {
        appRuntimeStatusListeners.add(listener)
    }

    /**
     * 移除app运行状态监听器
     */
    fun removeOnAppStatusChangeListener(listener: OnAppStatusChangeListener) {
        appRuntimeStatusListeners.remove(listener)
    }

    /**
     * app运行状态改变
     */
    interface OnAppStatusChangeListener {
        /**
         * app 进入前台，不包含app刚刚打开时进入前台
         */
        fun onAppEnterForeground()

        /**
         * app 进入后台
         */
        fun onAppEnterBackground()
    }

    /**
     * app登录状态回调
     */
    interface OnApplicationLoginListener {
        /**
         * app登录后回调
         */
        fun onApplicationLoginStateChange(isLogged: Boolean)
    }

    /**
     * 添加app登录状态监听器
     */
    fun addOnApplicationLoginListener(listener: OnApplicationLoginListener) {
        applicationLoginStateListeners.add(listener)
    }

    /**
     * 移除app登录状态监听器
     */
    fun removeOnApplicationLoginListener(listener: OnApplicationLoginListener) {
        applicationLoginStateListeners.remove(listener)
    }

    /**
     * 注册IM系统事件监听回调
     */
    fun addOnIMListener(imListener: IMListener) {
        eventIMListeners.add(imListener)
    }

    /**
     * 移除im监听器
     */
    fun removeOnIMListener(imListener: IMListener) {
        eventIMListeners.remove(imListener)
    }

    /**
     * 主页面需要显示badge的监听
     */
    interface OnShouldShowBadgeListener {
        /**
         * 回调时需要主页面在消息列表的图标上显示一个小红点以提示用户有未查看的消息
         */
        fun onShowBadge()
    }

    fun setOnShouldShowBadgeListener(listener: OnShouldShowBadgeListener) {
        shouldShowBadgeListener = listener
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

        // 每次登陆都必须初始化im sdk
        chatClientManager.initMobileIMSDK()
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

        // 发送退出登录包，并重置初始化标志符
        object : LogoutAsync(this) {}.execute()
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
         * 开始接收
         *
         * @param obtainStartOrDone
         * true 正在获取离线消息
         * false 离线消息获取完毕
         */
        fun onObtainOfflineState(obtainStartOrDone: Boolean)

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
        // im系统登陆成功
        if (code == 0) {
            // 消息列表显示拉取离线消息
            for (listener in eventIMListeners) {
                listener.onObtainOfflineState(true)
            }
            // 主页开始获取用户离线消息数量，若有，则为消息图片加上小红点
            imViewModel.getOfflineNumOnline(cachedToken).observeOnce {
                if (it) shouldShowBadgeListener?.onShowBadge()
                // 离线消息获取完成
                for (listener in eventIMListeners) {
                    listener.onObtainOfflineState(false)
                }
            }
        }
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
        // 消息丢失，将正在发送的这些消息状态变为发送失败，正在发送的箭头图标改为感叹号
        imViewModel.messagesLost(ArrayList<String>(lostMessages.size).also {
            for (lostMessage in lostMessages) {
                it.add(lostMessage.fp)
            }
        })
        for (listener in eventIMListeners) {
            listener.onIMMessageLost(lostMessages)
        }
    }

    override fun messagesBeReceived(fingerprint: String) {
        // 更新这条正在发送的消息的状态为发送成功，发送的箭头图标消失
        imViewModel.messageBeReceived(fingerprint)
        for (listener in eventIMListeners) {
            listener.onIMMessageBeReceived(fingerprint)
        }
    }

    override fun onRecieveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        // 保存在线消息
        cachedMyInfo?.let {
            imViewModel.saveReceivedMessage(
                    ChatHistory(
                            fingerprint,
                            senderID,
                            it.userId.toString(),
                            typeu,
                            content,
                            System.currentTimeMillis(),
                            ChatRecyclerAdapter.MessageSendStatus.SUCCESS))
            // 收到消息，通知主页面显示小红点
            shouldShowBadgeListener?.onShowBadge()
        }
        for (listener in eventIMListeners) {
            listener.onIMReceiveMessage(fingerprint, senderID, content, typeu)
        }
    }

    override fun onErrorResponse(errorCode: Int, message: String) {
        LogUtils.d("error -- > $errorCode $message")
        for (listener in eventIMListeners) {
            listener.onIMErrorResponse(errorCode, message)
        }
    }

    /**
     * 仅观察一次的观察者
     */
    fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }
}
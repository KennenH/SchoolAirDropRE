package com.example.schoolairdroprefactoredition.application

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.database.SARoomDatabase
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.im.IMClientManager
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.example.schoolairdroprefactoredition.viewmodel.ChatViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.MiPushClient
import kotlinx.android.synthetic.main.activity_chat.*
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

class SAApplication : Application(), ChatBaseEvent, MessageQoSEvent, ChatMessageEvent, LifecycleObserver {

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
        // 框架异步发送消息
        object : LocalDataSender.SendCommonDataAsync(content, userID, fingerprint, ConstantUtil.MESSAGE_TYPE_TEXT) {
            override fun onPostExecute(code: Int?) {
                if (code != 0) chatViewModel.updateMessageStatus(fingerprint, ChatRecyclerAdapter.MessageSendStatus.FAILED)
            }
        }.execute()
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

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val schoolAirdropDatabase by lazy {
        SARoomDatabase.getDatabase(this, applicationScope)
    }

    val chatRepository by lazy {
        DatabaseRepository(schoolAirdropDatabase.chatHistoryDao())
    }

    private val chatClientManager by lazy {
        IMClientManager.getInstance(this)
    }

    private val chatViewModel by lazy {
        ChatViewModel.ChatViewModelFactory(chatRepository, this).create(ChatViewModel::class.java)
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

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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
        val mJsonCacheUtil = JsonCacheUtil.getInstance()
        var settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache(false)
        AppCompatDelegate.setDefaultNightMode(if (settings.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setAppTheme(isDarkTheme: Boolean) {
        val mJsonCacheUtil = JsonCacheUtil.getInstance()
        var settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        if (settings == null) settings = UserSettingsCache(false)
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResume() {
        // app 进入前台
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPause() {
        // app 进入后台
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
        object : LogoutAsync(this@SAApplication) {}.execute()
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
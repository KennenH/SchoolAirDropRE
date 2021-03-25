package com.example.schoolairdroprefactoredition.scene.base

import android.animation.ObjectAnimator
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.cache.util.UserSettingsCacheUtil
import com.example.schoolairdroprefactoredition.custom.InAppFloatAnimator
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import net.x52im.mobileimsdk.server.protocal.Protocal
import kotlin.properties.Delegates


/**
 * app所有activity页面中的父类
 *
 * 用于后续添加通用方法
 */
open class BaseActivity : AppCompatActivity(), SAApplication.IMListener {

    companion object {
        /**
         * app中接收到消息时的弹窗
         */
        const val IM_FLOAT_TAG = "floatIMTag"

        /**
         * 浮窗显示持续时间
         */
        const val FLOAT_LAST = 2500L
    }

    private lateinit var soundPool: SoundPool

    private var soundId by Delegates.notNull<Int>()

    /**
     * 本页面是否正在活动
     *
     * 活动状态下打开app内弹窗收到消息将显示弹窗
     */
    private var isActive = false

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    private val dismissRunnable by lazy {
        Runnable {
            EasyFloat.dismiss(this@BaseActivity, IM_FLOAT_TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
        initRes()
    }

    override fun onResume() {
        super.onResume()
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        EasyFloat.dismiss(this)
        isActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListeners()
        releaseRes()
    }

    /**
     * 注册im事件回调
     */
    private fun initListeners() {
        (application as? SAApplication)?.addOnIMListener(this)
    }

    /**
     * 初始化声音资源
     */
    private fun initRes() {
        soundPool = SoundPool.Builder().build()
        soundId = soundPool.load(this, R.raw.message_sound, 1)
    }

    private fun removeListeners() {
        (application as? SAApplication)?.removeOnIMListener(this)
    }

    private fun releaseRes() {
        soundPool.release()
    }

    /**
     * 仅观察一次的观察者
     * 遵循context的生命周期
     */
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }

    /**
     * 仅观察一次的观察者
     * 任何时候数据变化都会使观察者返回数据
     */
    fun <T> LiveData<T>.observeAnywayOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }

    /**
     * 显示浮窗
     *
     * @param typeu 消息类型
     * one of
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.MESSAGE_TYPE_TEXT] 文本消息
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.MESSAGE_TYPE_IMAGE] 图片消息
     */
    private fun showFloatWindow(content: String, userID: String, typeu: Int) {
        // 当前activity可见且用户设置为app内弹窗则显示浮窗
        if (isActive && UserSettingsCacheUtil.getInstance().isShouldShowFloat()) {
            EasyFloat.isShow(this@BaseActivity, IM_FLOAT_TAG)?.let {
                if (it) { // 正在显示，直接替换文本，刷新显示时间
                    EasyFloat.getFloatView(this@BaseActivity, IM_FLOAT_TAG)?.apply {
                        findViewById<TextView?>(R.id.float_in_app_content)?.text =
                                if (typeu == ConstantUtil.MESSAGE_TYPE_TEXT) content
                                else getString(R.string.picture)

                        resetPostDelay(this)
                    }
                } else {
                    EasyFloat.with(this)
                            .setTag(IM_FLOAT_TAG)
                            .setLayout(R.layout.sheet_float_in_app) { view: View? ->
                                view?.parent?.requestDisallowInterceptTouchEvent(true)
                                view?.findViewById<TextView?>(R.id.float_in_app_content)?.text =
                                        if (typeu == ConstantUtil.MESSAGE_TYPE_TEXT) content
                                        else getString(R.string.picture)

                                view?.postDelayed(dismissRunnable, FLOAT_LAST)
                            }
                            .registerCallback {
                                var downY = 0f // 按下时的y
                                var isClick = true
                                touchEvent { view, motionEvent ->
                                    resetPostDelay(view)
                                    when (motionEvent.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            isClick = true
                                            downY = motionEvent.y
                                        }
                                        MotionEvent.ACTION_MOVE -> {
                                            isClick = false
                                            val dMoveY = motionEvent.y - downY
                                            if (dMoveY <= 0) {
                                                view.translationY = dMoveY
                                            } else {
                                                view.translationY = dMoveY * (0.1f - 0.000002f * dMoveY)
                                            }
                                        }
                                        MotionEvent.ACTION_UP -> {
                                            if (isClick) {
                                                EasyFloat.dismiss(this@BaseActivity, IM_FLOAT_TAG)
                                                ChatActivity.start(this@BaseActivity, userID.toInt())
                                            } else {
                                                if (view.translationY >= 0) {
                                                    // 被下拉时放手将会回归原位
                                                    floatResumePosition(view)
                                                } else {
                                                    // 上拉时放手将会dismiss
                                                    EasyFloat.dismiss(this@BaseActivity, IM_FLOAT_TAG)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            .setFilter(ChatActivity::class.java)
                            .setAnimator(InAppFloatAnimator())
                            .setMatchParent(widthMatch = true, heightMatch = false)
                            .setDragEnable(false)
                            .setSidePattern(SidePattern.TOP)
                            .setShowPattern(ShowPattern.CURRENT_ACTIVITY)
                            .show()
                }
            }
        }
    }

    /**
     * 播放系统声音提示
     */
    private fun playSystemNotification() {
        if (UserSettingsCacheUtil.getInstance().isShouldPlaySystemNotification()) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
//            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            RingtoneManager.getRingtone(applicationContext, notification).play()
        }
    }

    /**
     * 重置view的post delay时间
     */
    private fun resetPostDelay(view: View?) {
        view?.removeCallbacks(dismissRunnable)
        view?.postDelayed(dismissRunnable, FLOAT_LAST)
    }

    /**
     * 将浮窗归位
     */
    private fun floatResumePosition(view: View) {
        ObjectAnimator.ofFloat(
                view,
                "translationY",
                view.translationY,
                0f).apply {
            duration = 150
            interpolator = DecelerateInterpolator(2f)
            addUpdateListener {
                view.translationY = it.animatedValue as Float
            }
            start()
        }
    }

    override fun onIMStartLogin() {
    }

    override fun onIMLoginResponse(code: Int) {
    }

    override fun onObtainOfflineState(obtainStartOrDone: Boolean) {
    }

    override fun onIMLinkDisconnect(code: Int) {
    }

    override fun onIMMessageLost(lostMessages: ArrayList<Protocal>) {
    }

    override fun onIMMessageBeReceived(fingerprint: String) {
    }

    override fun onIMReceiveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        playSystemNotification()
        showFloatWindow(content, senderID, typeu)
    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
    }
}
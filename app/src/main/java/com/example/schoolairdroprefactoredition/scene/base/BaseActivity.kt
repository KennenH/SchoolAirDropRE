package com.example.schoolairdroprefactoredition.scene.base

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.custom.InAppFloatAnimator
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import net.x52im.mobileimsdk.server.protocal.Protocal

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

    private val dismissRunnable by lazy {
        Runnable {
            EasyFloat.dismiss(this@BaseActivity, IM_FLOAT_TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
    }

    override fun onPause() {
        super.onPause()
        EasyFloat.dismiss(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListeners()
    }

    /**
     * ！！！！
     *  注意
     * ！！！！
     * 所有在这个Activity基类中注册的listener在子类activity中千万不要再注册，否则每次回调都会有两次
     */
    private fun initListeners() {
        (application as? SAApplication)?.addOnIMListener(this)
    }

    private fun removeListeners() {
        (application as? SAApplication)?.removeOnIMListener(this)
    }

    /**
     * 仅观察一次的观察者
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
     * 显示浮窗
     */
    private fun showFloatWindow(content: String, userID: String) {
        EasyFloat.isShow(this@BaseActivity, IM_FLOAT_TAG)?.let {
            if (it) { // 正在显示，直接替换文本，刷新显示时间
                EasyFloat.getFloatView(this@BaseActivity, IM_FLOAT_TAG)?.apply {
                    findViewById<TextView?>(R.id.float_in_app_content)?.text = content
                    resetPostDelay(this)
                }
            } else {
                EasyFloat.with(this)
                        .setTag(IM_FLOAT_TAG)
                        .setLayout(R.layout.sheet_float_in_app) { view: View? ->
                            view?.parent?.requestDisallowInterceptTouchEvent(true)
                            view?.findViewById<TextView?>(R.id.float_in_app_content)?.text = content
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
        showFloatWindow(content, senderID)
    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
    }
}
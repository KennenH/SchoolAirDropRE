package com.example.schoolairdroprefactoredition.scene.base

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.BarUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.utils.MyUtil.loading
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import com.lxj.xpopup.impl.LoadingPopupView
import me.jessyan.autosize.AutoSizeCompat

open class ImmersionStatusBarActivity : BaseActivity() {
    private var mLoading: LoadingPopupView? = null

    /**
     * 是否为暗黑模式
     */
    protected var isDarkTheme = false

    /**
     * 是否有请求正在运行标志位，当有请求正在运行时通过
     * [ImmersionStatusBarActivity.showLoading]
     * 运行的网络请求将不会再次发起并且会被抛弃而不是等待上一个请求结束
     */
    private var isRequestRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            isDarkTheme = true
        }
        setActivityTheme()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected open fun setActivityTheme() {
        StatusBarUtil.setStatusTextColor(this, getColor(R.color.white), !isDarkTheme)
        BarUtils.setStatusBarLightMode(this, !isDarkTheme)
        BarUtils.setNavBarLightMode(this, !isDarkTheme)
        BarUtils.setNavBarColor(this, getColor(R.color.white))
    }

    protected fun showLoading() {
        mLoading.let {
            if (it == null) {
                mLoading = loading(this)
            }

            mLoading?.show()
        }
    }

    /**
     * request在获取数据结束之前将只会被发起一次
     *
     *
     * 只有自动调用的dismissloading才会重置isRequestRunning
     * 手动将无法重置isReqeustRunning
     *
     * @param request 网络请求
     */
    protected fun showLoading(request: Runnable) {
        showLoading()
        if (!isRequestRunning) {
            isRequestRunning = true
            request.run()
        }
    }

    protected fun updateLoadingTip(tip: String) {
        mLoading?.setTitle(tip)
    }

    protected fun dismissLoading() {
        isRequestRunning = false
        mLoading?.smartDismiss()
    }

    protected fun dismissLoading(task: Runnable) {
        isRequestRunning = false

        mLoading.let {
            if (it != null) {
                it.dismissWith(task)
            } else {
                task.run()
            }
        }
    }

    /**
     * 帮助androidAutoSize适配屏幕
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }
}
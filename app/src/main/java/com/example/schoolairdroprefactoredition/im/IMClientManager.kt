package com.example.schoolairdroprefactoredition.im

import android.app.Application
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import net.x52im.mobileimsdk.android.ClientCoreSDK
import net.x52im.mobileimsdk.android.conf.ConfigEntity

class IMClientManager private constructor(private var application: Application?) {

    companion object {
        private var INSTANCE: IMClientManager? = null
        fun getInstance(application: Application?) = INSTANCE
                ?: IMClientManager(application).also {
                    INSTANCE = it
                }
    }

    /**
     * 是否已经过初始化
     */
    private var init = false

    /**
     * 初始化im sdk，每次登录前必须保证调用成功此方法，否则会报203未初始化错误
     */
    @Synchronized
    fun initMobileIMSDK() {
        if (!init) {
            // 设置AppKey
            ConfigEntity.appKey = "5418023dfd98c579b6001741"

            // 设置服务器ip和服务器端口
            ConfigEntity.serverIP = ConstantUtil.SCHOOL_AIR_DROP_IP
            ConfigEntity.serverPort = 8901

            // MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_10S);

            // 开启/关闭DEBUG信息输出
            ClientCoreSDK.DEBUG = AppConfig.IS_DEBUG

            // 【特别注意】请确保首先进行核心库的初始化（这是不同于iOS和Java端的地方)
            ClientCoreSDK.getInstance().init(application)

            // 设置事件回调
            if (application is SAApplication) {
                ClientCoreSDK.getInstance().chatBaseEvent = application as SAApplication
                ClientCoreSDK.getInstance().chatMessageEvent = application as SAApplication
                ClientCoreSDK.getInstance().messageQoSEvent = application as SAApplication
            }
            init = true
        }
    }

    /**
     * 重置[init]标志位
     */
    fun resetInit() {
        init = false
    }

    /**
     * 是否已经初始化，即类似于判断是否已登录im
     */
    fun isInit() = init
}
package com.example.schoolairdroprefactoredition.im

import android.content.Context
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import net.x52im.mobileimsdk.android.ClientCoreSDK
import net.x52im.mobileimsdk.android.conf.ConfigEntity

class IMClientManager private constructor(private var context: Context?) {

    companion object {
        private var INSTANCE: IMClientManager? = null
        fun getInstance(context: Context?) = INSTANCE
                ?: IMClientManager(context).also {
                    INSTANCE = it
                }
    }

    /**
     * 是否已经过初始化
     */
    private var init = false

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
            ClientCoreSDK.DEBUG = true

            // 【特别注意】请确保首先进行核心库的初始化（这是不同于iOS和Java端的地方)
            ClientCoreSDK.getInstance().init(context)

            // 设置事件回调
            if (context is SAApplication) {
                ClientCoreSDK.getInstance().chatBaseEvent = context as SAApplication
                ClientCoreSDK.getInstance().chatMessageEvent = context as SAApplication
                ClientCoreSDK.getInstance().messageQoSEvent = context as SAApplication
            }
            init = true
        }
    }

    init {
        initMobileIMSDK()
    }

    /**
     * 重置[init]标志位
     */
    fun reflagInit() {
        init = false
    }
}
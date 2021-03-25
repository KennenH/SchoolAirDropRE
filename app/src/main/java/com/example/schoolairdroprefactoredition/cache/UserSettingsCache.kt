package com.example.schoolairdroprefactoredition.cache

/**
 * 用户本地偏好设置
 *
 * 使用[com.example.schoolairdroprefactoredition.cache.util.UserSettingsCacheUtil]来使用本类
 */
data class UserSettingsCache(
        /**
         * 是否开启暗黑模式
         */
        var isDarkTheme: Boolean = false,

        /**
         * 是否显示app内弹窗
         */
        var isDisplayInAppFloat: Boolean = true,

        /**
         * 是否播放系统提示音
         */
        var isPlaySystemNotification: Boolean = true
) {

    companion object {
        const val KEY = "user_settings_cache"
    }

}
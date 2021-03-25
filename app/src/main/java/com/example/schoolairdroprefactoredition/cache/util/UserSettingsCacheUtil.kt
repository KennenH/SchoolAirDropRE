package com.example.schoolairdroprefactoredition.cache.util

import com.example.schoolairdroprefactoredition.cache.UserSettingsCache


/**
 * 用户偏好设置帮助类
 * 便于使用[UserSettingsCache]
 */
class UserSettingsCacheUtil private constructor() {

    companion object {
        private var INSTANCE: UserSettingsCacheUtil? = null
        fun getInstance() = INSTANCE
                ?: UserSettingsCacheUtil().also {
                    INSTANCE = it
                }
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 获取用户是否设置应该显示app内弹窗
     */
    fun isShouldShowFloat(): Boolean {
        return jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?.isDisplayInAppFloat ?: true
    }

    /**
     * 保存用户对于app内弹窗的设置
     */
    fun saveShouldShowFloat(shouldShowFloat: Boolean) {
        val settings = jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?: UserSettingsCache()
        jsonCacheUtil.saveCache(UserSettingsCache.KEY, settings.also {
            it.isDisplayInAppFloat = shouldShowFloat
        })
    }

    /**
     * 获取用户是否设置暗黑模式
     */
    fun isDarkTheme(): Boolean {
        return jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?.isDarkTheme ?: false
    }

    /**
     * 保存用户对于app暗黑模式的设置
     */
    fun saveDarkTheme(isDarkTheme: Boolean) {
        val settings = jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?: UserSettingsCache()
        jsonCacheUtil.saveCache(UserSettingsCache.KEY, settings.also {
            it.isDarkTheme = isDarkTheme
        })
    }

    /**
     * 获取用户是否设置播放系统声音
     */
    fun isShouldPlaySystemNotification(): Boolean {
        return jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?.isPlaySystemNotification ?: true
    }

    /**
     * 保存用户对于播放系统声音的设置
     */
    fun saveShouldPlaySystemNotification(shouldPlaySystemNotification: Boolean) {
        val settings = jsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
                ?: UserSettingsCache()
        jsonCacheUtil.saveCache(UserSettingsCache.KEY, settings.also {
            it.isPlaySystemNotification = shouldPlaySystemNotification
        })
    }
}
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import okhttp3.internal.notify

class SettingsViewModel : ViewModel() {

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 是否为暗黑主题
     */
    fun isDarkTheme(): Boolean {
        val setting = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        return setting?.isDarkTheme ?: false
    }

    /**
     * 切换app内置浮窗是否显示
     */
    fun toggleDisplayFloat() {
        val setting = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        setting.let {
            if (it == null) {
                // 若不曾设置，则应该是默认的true，因此这里new一个对象并将其改成false
                mJsonCacheUtil.saveCache(UserSettingsCache.KEY, UserSettingsCache(isDisplayInAppFloat = false))
            } else {
                // 若存在设置，则保存反值
                mJsonCacheUtil.saveCache(UserSettingsCache.KEY, it.also { it.isDisplayInAppFloat = !it.isDisplayInAppFloat })
            }
        }
    }

    /**
     * 获取当前app是否需要显示内置浮窗
     */
    fun getIsDisplayFloat(): Boolean {
        val settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        settings.let {
            // 若不曾设置应该是默认的true，因此为null的时候也是显示
            return it == null || it.isDisplayInAppFloat
        }
    }
}
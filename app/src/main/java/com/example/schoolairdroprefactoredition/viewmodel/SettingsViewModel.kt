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
    fun isDarkTheme(): Boolean? {
        val setting = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        return setting?.isDarkTheme
    }

    /**
     * 切换app内置浮窗是否显示
     */
    fun toggleDisplayFloat() {
        val setting = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
        setting.let {
            if (it == null) {
                // 若不曾设置，则应该是默认的false，因此这里改成true
                mJsonCacheUtil.saveCache(UserSettingsCache.KEY, UserSettingsCache(isDisplayInAppFloat = true))
            } else {
                // 若存在设置，则将现有的设置取反值保存
                mJsonCacheUtil.saveCache(UserSettingsCache.KEY, setting?.isDisplayInAppFloat?.not())
            }
        }
    }
}
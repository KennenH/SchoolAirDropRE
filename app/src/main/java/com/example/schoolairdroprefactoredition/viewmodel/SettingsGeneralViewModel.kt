package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import kotlinx.coroutines.launch

class SettingsGeneralViewModel : ViewModel() {

    private val mIsDarkTheme = MutableLiveData<Boolean>()

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.newInstance()
    }

    /**
     * 是否为暗黑主题
     */
    fun getIsDarkTheme(): LiveData<Boolean> {
        viewModelScope.launch {
            val settings = mJsonCacheUtil.getValue(UserSettingsCache.KEY, UserSettingsCache::class.java)
            mIsDarkTheme.value = settings?.isDarkTheme
        }
        return mIsDarkTheme
    }
}
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SettingsGeneralViewModel : ViewModel() {

    private val mIsDarkTheme = MutableLiveData<Boolean>()

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 是否为暗黑主题
     */
    fun getIsDarkTheme(): LiveData<Boolean> {
        viewModelScope.launch {
            val settings = mJsonCacheUtil.getCache(UserSettingsCache.KEY, UserSettingsCache::class.java)
            mIsDarkTheme.postValue(settings?.isDarkTheme)
        }
        return mIsDarkTheme
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
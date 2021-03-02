package com.example.schoolairdroprefactoredition.cache

data class UserSettingsCache(var isDarkTheme: Boolean = false, var isDisplayInAppFloat: Boolean = true) {

    companion object {
        const val KEY = "user_settings_cache"
    }

}
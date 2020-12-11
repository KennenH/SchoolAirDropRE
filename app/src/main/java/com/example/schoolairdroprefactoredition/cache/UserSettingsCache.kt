package com.example.schoolairdroprefactoredition.cache

data class UserSettingsCache(var isDarkTheme: Boolean) {

    companion object {
        const val KEY = "user_settings_cache"
    }

}
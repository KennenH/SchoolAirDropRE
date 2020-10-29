package com.example.schoolairdroprefactoredition.cache

data class UserSettingsCache(var isDarkTheme: Boolean) {

    companion object {
        const val USER_SETTINGS = "user_settings_cache"
    }

}
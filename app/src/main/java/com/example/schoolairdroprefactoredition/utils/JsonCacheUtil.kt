package com.example.schoolairdroprefactoredition.utils

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.blankj.utilcode.util.Utils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.cache.CacheWithDuration
import com.google.gson.Gson

/**
 * 使用SP保存数据
 *
 *
 * 关于此类的常量定义类[JsonCacheConstantUtil]
 */
class JsonCacheUtil private constructor() {

    companion object {
        const val ENCRYPTED_SHARED_PREFERENCE_NAME = "?school_aridrop_encrypted_preference"

        private var jsonCacheUtil: JsonCacheUtil? = null
        fun getInstance() = jsonCacheUtil
                ?: JsonCacheUtil().also {
                    jsonCacheUtil = it
                }

        /**
         * 带检查频繁操作的运行
         *
         * 检查触发时间默认为10秒，触发5次检查将进入一次CoolDown，一次为15秒，CoolDown期间触发任何检查都将重置
         * 15秒的CoolDown，直到用户在15秒内没有触发任何检查为止
         *
         * @param runIfNotFrequent 在没有触发CoolDown时执行的操作
         * @param runIfFrequent    在触发CoolDown时执行的操作
         */
        fun runWithFrequentCheck(runIfNotFrequent: Runnable?, runIfFrequent: Runnable? = null) {
            val jsonCacheUtil = getInstance()
            val refreshCount = jsonCacheUtil.getCache(JsonCacheConstantUtil.FREQUENT_ACTION_COUNT, Int::class.java)
            if (refreshCount == null || refreshCount < 5) {
                // 操作未触发CoolDown
                runIfNotFrequent?.run()
            } else {
                // 操作过于频繁，触发或重置CoolDown
                runIfFrequent?.run()
                DialogUtil.showCenterDialog(Utils.getApp().applicationContext, DialogUtil.DIALOG_TYPE.FAILED, R.string.actionTooFrequent)
            }
            // 无论有没有放行执行被检查的语句，都会重置冷却时间，直到用户没有触发检查操作15秒为止
            jsonCacheUtil.saveCache(JsonCacheConstantUtil.FREQUENT_ACTION_COUNT, refreshCount?.plus(1)
                    ?: 1, JsonCacheConstantUtil.ACTION_TOO_FREQUENT_COOLDOWN)
        }
    }

    private val mSharePreferences by lazy {
        val context = Utils.getApp().applicationContext
        EncryptedSharedPreferences
                .create(context,
                        ENCRYPTED_SHARED_PREFERENCE_NAME,
                        MasterKey.Builder(context).setKeyGenParameterSpec(MasterKeys.AES256_GCM_SPEC).build(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    private val mGson by lazy {
        Gson()
    }

    /**
     * 保存键值对，有效时间duration
     *
     * @param key      键
     * @param value    值
     * @param duration 有效时间 单位ms
     */
    @JvmOverloads
    fun saveCache(key: String?, value: Any?, duration: Long = -1L) {
        var durationVar = duration
        val editor = mSharePreferences.edit()
        val gsonStr = mGson.toJson(value)
        if (durationVar != -1L) {
            durationVar += System.currentTimeMillis()
        }
        val cacheWithDuration = CacheWithDuration(durationVar, gsonStr)
        val cacheWithDurationToJson = mGson.toJson(cacheWithDuration)
        editor.putString(key, cacheWithDurationToJson)
        editor.apply()
    }

    /**
     * 删除键对应的值
     *
     * @param key 要删除的值对应的键
     */
    fun deleteCache(key: String?) {
        mSharePreferences.edit().remove(key).apply()
    }

    /**
     * 获取对于键的值缓存
     *
     * @param key   键
     * @param clazz 值所对应的类
     * @return 键对应的值
     */
    fun <T> getCache(key: String?, clazz: Class<T>?): T? {
        val valueWithDuration = mSharePreferences.getString(key, null) ?: return null
        val (duration, cache) = mGson.fromJson(valueWithDuration, CacheWithDuration::class.java)
        return if (duration != -1L && duration - System.currentTimeMillis() <= 0) { // 已过期
            null
        } else { // 未过期
            mGson.fromJson(cache, clazz)
        }
    }
}
package com.example.schoolairdroprefactoredition.cache

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.blankj.utilcode.util.Utils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.utils.DialogUtil
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
         * 触发有一定的条件，触发后惩罚较大
         *
         * 检查触发时间默认为[JsonCacheConstantUtil.FREQUENT_ACTION_COOLDOWN]秒，触发5次检查将进入一次CoolDown，一次为
         * [JsonCacheConstantUtil.FREQUENT_ACTION_COOLDOWN]秒
         *
         * @param runIfNotFrequent 在没有触发CoolDown时执行的操作
         * @param runIfFrequent    在触发CoolDown时执行的操作
         */
        @JvmStatic
        fun runWithFrequentCheck(context: Context?, runIfNotFrequent: Runnable?, runIfFrequent: Runnable? = null) {
            val jsonCacheUtil = getInstance()
            val refreshCount = jsonCacheUtil.getCache(JsonCacheConstantUtil.FREQUENT_ACTION_COUNT, Int::class.java)
            if (refreshCount == null || refreshCount < 5) {
                // 操作未触发CoolDown
                runIfNotFrequent?.run()
                // 直到用户在冷却时间内没有触发检查操作为止
                jsonCacheUtil.saveCache(
                        JsonCacheConstantUtil.FREQUENT_ACTION_COUNT,
                        refreshCount?.plus(1) ?: 1,
                        JsonCacheConstantUtil.FREQUENT_ACTION_COOLDOWN)
            } else {
                // 操作过于频繁，触发或重置CoolDown
                runIfFrequent?.run()
                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.actionTooFrequent)
            }
        }

        /**
         * 带检查过于快速操作的运行
         * 同样是接口调用保护，无条件触发，对于正常操作几乎没有影响
         */
        @JvmStatic
        fun runWithTooQuickCheck(runIfNotTooQuick: Runnable?) {
            val jsonCacheUtil = getInstance()
            val tooQuick = jsonCacheUtil.getCache(JsonCacheConstantUtil.QUICK_ACTION_FLAG, Boolean::class.java)
            // 非快速操作，执行，快速操作将直接被过滤而不是作为其他处理
            if (tooQuick == null) {
                runIfNotTooQuick?.run()
                jsonCacheUtil.saveCache(
                        JsonCacheConstantUtil.QUICK_ACTION_FLAG,
                        true,
                        JsonCacheConstantUtil.QUICK_ACTION_COOLDOWN)
            }
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
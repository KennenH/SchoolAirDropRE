package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import com.blankj.utilcode.util.Utils;
import com.example.schoolairdroprefactoredition.cache.CacheWithDuration;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 使用SP保存数据
 */
public class JsonCacheUtil {

    public static final String ENCRYPTED_SHARED_PREFERENCE_NAME = "share_preference_name";

    private static JsonCacheUtil jsonCacheUtil = null;
    private SharedPreferences mSharePreferences;
    private final Gson mGson;

    private JsonCacheUtil() {
        try {
            Context context = Utils.getApp().getApplicationContext();
            mSharePreferences = EncryptedSharedPreferences
                    .create(context,
                            ENCRYPTED_SHARED_PREFERENCE_NAME,
                            new MasterKey.Builder(context).setKeyGenParameterSpec(MasterKeys.AES256_GCM_SPEC).build(),
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        mGson = new Gson();
    }

    /**
     * 保存键值对
     *
     * @param key   键
     * @param value 值
     */
    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    /**
     * 保存键值对，有效时间duration
     *
     * @param key      键
     * @param value    值
     * @param duration 有效时间 ms
     */
    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharePreferences.edit();
        String gsonStr = mGson.toJson(value);

        if (duration != -1L) {
            duration += System.currentTimeMillis();
        }

        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, gsonStr);
        String cacheWithDurationToJson = mGson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithDurationToJson);
        editor.apply();
    }

    /**
     * 删除键对应的值
     *
     * @param key 要删除的值对应的键
     */
    public void deleteCache(String key) {
        mSharePreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharePreferences.getString(key, null);
        if (valueWithDuration == null) return null;

        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        long duration = cacheWithDuration.getDuration();
        if (duration != -1L && duration - System.currentTimeMillis() <= 0) { // 已过期
            return null;
        } else { // 未过期
            String cache = cacheWithDuration.getJsonCache();
            return mGson.fromJson(cache, clazz);
        }
    }

    public static JsonCacheUtil newInstance() {
        if (jsonCacheUtil == null) {
            jsonCacheUtil = new JsonCacheUtil();
        }
        return jsonCacheUtil;
    }

}

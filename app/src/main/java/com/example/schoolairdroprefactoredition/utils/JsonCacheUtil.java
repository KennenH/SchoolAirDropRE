package com.example.schoolairdroprefactoredition.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.Utils;
import com.example.schoolairdroprefactoredition.cache.CacheWithDuration;
import com.google.gson.Gson;

/**
 * 使用SP保存数据
 */
public class JsonCacheUtil {

    public static final String SHARED_PREFERENCE_NAME = "share_preference_name";

    private static JsonCacheUtil jsonCacheUtil = null;
    private final SharedPreferences mSharePreferences;
    private Gson mGson;


    private JsonCacheUtil() {
        mSharePreferences = Utils.getApp().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
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
     * 保存键值对，有效时间位duration
     *
     * @param key      键
     * @param value    值
     * @param duration 有效时间
     */
    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharePreferences.edit();
        String gsonStr = mGson.toJson(value);

        if (duration != -1L)
            duration += System.currentTimeMillis();

        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, gsonStr);
        String cacheWithDurationToJson = mGson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithDurationToJson);
        editor.apply();
    }

    /**
     * 删除
     *
     * @param key
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

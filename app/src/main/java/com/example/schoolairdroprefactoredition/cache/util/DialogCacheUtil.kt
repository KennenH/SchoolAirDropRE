package com.example.schoolairdroprefactoredition.cache.util

import com.example.schoolairdroprefactoredition.cache.DialogCache
import com.example.schoolairdroprefactoredition.utils.DialogUtil


/**
 * @author kennen
 * @date 2021/3/26
 */
class DialogCacheUtil private constructor() {

    companion object {
        private var INSTANCE: DialogCacheUtil? = null

        fun getInstance() =
                INSTANCE ?: DialogCacheUtil().also {
                    INSTANCE = it
                }
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 用户是否同意了协议
     */
    fun isAgreeToPolicy(): Boolean {
        return jsonCacheUtil.getCache(DialogCache.KEY_IS_AGREE_TO_POLICY, Boolean::class.java)
                ?: false
    }

    /**
     * 保存用户对于协议已经同意
     */
    fun saveAgreeToPolicy() {
        jsonCacheUtil.saveCache(DialogCache.KEY_IS_AGREE_TO_POLICY, true)
    }

    /**
     * 用户是否已经知晓安卓app无法收到系统通知
     */
    fun isKnowMessageTip(): Boolean {
        return jsonCacheUtil.getCache(DialogCache.KEY_ID_KNOW_MESSAGE_TIP, Boolean::class.java)
                ?: false
    }

    /**
     * 保存用户已知晓安卓app无法收到通知
     */
    fun saveIsKnowMessageTip() {
        jsonCacheUtil.saveCache(DialogCache.KEY_ID_KNOW_MESSAGE_TIP, true)
    }
}
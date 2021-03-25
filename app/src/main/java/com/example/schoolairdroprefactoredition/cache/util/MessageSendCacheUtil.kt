package com.example.schoolairdroprefactoredition.cache.util

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.example.schoolairdroprefactoredition.cache.MessageSendCheckCache

/**
 * 带检查的发送消息辅助类，若发现用户发送消息速度过快将拦截
 *
 * 便于快速使用[com.example.schoolairdroprefactoredition.cache.MessageSendCheckCache]
 */
class MessageSendCacheUtil private constructor() {

    companion object {
        private var INSTANCE: MessageSendCacheUtil? = null

        fun getInstance() = INSTANCE
                ?: MessageSendCacheUtil().also {
                    INSTANCE = it
                }
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 检查消息是否发送过于频繁
     * 多发必须满足所有消息都能成功发送，否则将拦截这一整批消息
     *
     */
    fun saveMessageWithCheck(): Boolean {
        val isCoolDown = jsonCacheUtil.getCache(MessageSendCheckCache.KEY_MESSAGE_FREQUENT_COOL_DOWN, Boolean::class.java)
        // 当前仍然处于消息发送冷却时间内
        if (isCoolDown != null) {
            return false
        }

        val cache = jsonCacheUtil.getCache(MessageSendCheckCache.KEY, MessageSendCheckCache::class.java)
                ?: MessageSendCheckCache(ArrayDeque())

        cache.latestMessagesTimeList.apply {
//            for (i in 0..messages) {
                addLast(System.currentTimeMillis())
//            }

            if (size > 10) {
                // 如果超过10条就把最开始的一条去掉，防止恶意拉长计算总时间
                removeFirst()
                val meanSpeed = TimeUtils.getTimeSpan(last(), first(), TimeConstants.MSEC) < MessageSendCheckCache.MESSAGE_SEND_10_MIN_SPAN
                if (meanSpeed) {
                    // 标志当前已经入消息发送冷却时间
                    jsonCacheUtil.saveCache(MessageSendCheckCache.KEY_MESSAGE_FREQUENT_COOL_DOWN, true, MessageSendCheckCache.MESSAGE_FREQUENT_COOLDOWN)
                    return false
                }
            }
        }

        jsonCacheUtil.saveCache(MessageSendCheckCache.KEY, cache, MessageSendCheckCache.MESSAGE_CHECK_DURATION)
        return true
    }
}
package com.example.schoolairdroprefactoredition.cache.util

import com.blankj.utilcode.constant.TimeConstants
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
     * 检查文字消息是否发送过快
     */
    fun checkTextMessageSendFrequent(): Boolean {
        val isCoolingDown = jsonCacheUtil.getCache(MessageSendCheckCache.KEY_TEXT_MESSAGE_FREQUENT_COOL_DOWN, Boolean::class.java)
        // 当前仍然处于消息发送冷却时间内
        if (isCoolingDown != null) {
            return false
        }

        val continuous = jsonCacheUtil.getCache(MessageSendCheckCache.KEY, MessageSendCheckCache::class.java)
                ?: MessageSendCheckCache(ArrayDeque(), ArrayDeque())

        continuous.continuousTextMessagesTimeList.apply {
            addLast(System.currentTimeMillis())
            if (size > 10) {
                // 如果超过10条就把最开始的一条去掉，防止恶意拉长计算总时间
                removeFirst()
                val meanSpeed = TimeUtils.getTimeSpan(last(), first(), TimeConstants.MSEC) < MessageSendCheckCache.MESSAGE_SEND_10_TEXT_MIN_SPAN
                if (meanSpeed) {
                    // 标志当前已经入消息发送冷却时间
                    jsonCacheUtil.saveCache(MessageSendCheckCache.KEY_TEXT_MESSAGE_FREQUENT_COOL_DOWN, true, MessageSendCheckCache.MESSAGE_TEXT_FREQUENT_COOLDOWN)
                    return false
                }
            }
        }

        // 刷新连续发送
        jsonCacheUtil.saveCache(MessageSendCheckCache.KEY, continuous, MessageSendCheckCache.MESSAGE_TEXT_CHECK_DURATION)
        return true
    }

    /**
     * 检查图片消息发送是否过快
     *
     * 图片多发途中触发拦截时将允许用户发送未被拦截的部分，返回可以发送图片的张数
     *
     * @param imageSize 这一批图片消息的数量
     * @return 返回这一批消息中可以被发送的图片的数量
     */
    fun checkImageMessageSendFrequent(imageSize: Int): Int {
        val isCoolingDown = jsonCacheUtil.getCache(MessageSendCheckCache.KEY_IMAGE_MESSAGE_FREQUENT_COOL_DOWN, Boolean::class.java)
        // 当前仍然处于图片消息发送冷却时间内
        if (isCoolingDown != null) {
            return 0
        }

        val continuous = jsonCacheUtil.getCache(MessageSendCheckCache.KEY, MessageSendCheckCache::class.java)
                ?: MessageSendCheckCache(ArrayDeque(), ArrayDeque())

        for (canBeSent in 0 until imageSize) {
            continuous.continuousImageMessagesTimeList.apply {
                addLast(System.currentTimeMillis())
                if (this.size > 10) {
                    // 如果超过10条就把最开始的一条去掉
                    removeFirst()
                    val meanSpeed = TimeUtils.getTimeSpan(last(), first(), TimeConstants.MSEC) < MessageSendCheckCache.MESSAGE_SEND_10_IMAGE_MIN_SPAN
                    if (meanSpeed) {
                        // 标志当前已进入消息发送冷却时间
                        jsonCacheUtil.saveCache(MessageSendCheckCache.KEY_IMAGE_MESSAGE_FREQUENT_COOL_DOWN, true, MessageSendCheckCache.MESSAGE_IMAGE_FREQUENT_COOLDOWN)
                        return canBeSent
                    }
                }
            }
            // 刷新连续发送
            jsonCacheUtil.saveCache(MessageSendCheckCache.KEY, continuous, MessageSendCheckCache.MESSAGE_IMAGE_CHECK_DURATION)
        }

        return imageSize
    }
}
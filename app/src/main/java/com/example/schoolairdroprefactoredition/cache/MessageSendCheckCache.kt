package com.example.schoolairdroprefactoredition.cache

import kotlin.collections.ArrayDeque

/**
 * 通过[com.example.schoolairdroprefactoredition.cache.util.MessageSendCacheUtil]来使用该类
 */
data class MessageSendCheckCache(
        /**
         * 连续发送的文字消息时间列表
         */
        val continuousTextMessagesTimeList: ArrayDeque<Long>,

        /**
         * 连续发送的图片消息时间列表
         */
        val continuousImageMessagesTimeList: ArrayDeque<Long>
) {
    companion object {

        const val KEY = "message_send_check"

        /**
         * 触发文字消息检查后的冷却标志
         */
        const val KEY_TEXT_MESSAGE_FREQUENT_COOL_DOWN = "toofrequenttextmessagesend!"

        /**
         * 触发图片消息检查的冷却标志
         */
        const val KEY_IMAGE_MESSAGE_FREQUENT_COOL_DOWN = "toofrequentimagemessagesend!"

        /**
         * 当触发文字冷却时的禁言时间
         */
        const val MESSAGE_TEXT_FREQUENT_COOLDOWN = 10_000L

        /**
         * 触发图片冷却时间禁言时间
         */
        const val MESSAGE_IMAGE_FREQUENT_COOLDOWN = 20_000L

        /**
         * 前后两条文字消息判断为连续发送的最大时间间隔
         */
        const val MESSAGE_TEXT_CHECK_DURATION = 5_000L

        /**
         * 前后两条图片消息判断为连续发送的最大时间间隔
         */
        const val MESSAGE_IMAGE_CHECK_DURATION = 20_000L

        /**
         * 发送10条文字消息小于该时间将触发频繁发送警告
         */
        const val MESSAGE_SEND_10_TEXT_MIN_SPAN = 25_000L

        /**
         * 发送10条图片消息小于该事件将触发频繁发送警告
         */
        const val MESSAGE_SEND_10_IMAGE_MIN_SPAN = 60_000L
    }
}

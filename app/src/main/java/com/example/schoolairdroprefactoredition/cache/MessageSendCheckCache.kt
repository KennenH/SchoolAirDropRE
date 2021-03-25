package com.example.schoolairdroprefactoredition.cache

import kotlin.collections.ArrayDeque

/**
 * 通过[com.example.schoolairdroprefactoredition.cache.util.MessageSendCacheUtil]来使用该类
 */
data class MessageSendCheckCache(
        /**
         * 连续发送的消息时间列表
         */
        val latestMessagesTimeList: ArrayDeque<Long>
) {
    companion object {

        const val KEY = "message_send_check"

        /**
         * 触发检查后的冷却时间
         */
        const val KEY_MESSAGE_FREQUENT_COOL_DOWN = "toofrequentmessagesend!"

        /**
         * 当触发冷却时的禁言时间
         */
        const val MESSAGE_FREQUENT_COOLDOWN = 8_000L

        /**
         * 实例被保存之后的有效时间
         *
         * 当有新的消息发送时有效时间将被刷新，有效时间内未触发冷却本次检查计算自动结束
         */
        const val MESSAGE_CHECK_DURATION = 5_000L

        /**
         * 发送10条消息小于该时间将触发频繁发送警告
         */
        const val MESSAGE_SEND_10_MIN_SPAN = 20_000L
    }
}

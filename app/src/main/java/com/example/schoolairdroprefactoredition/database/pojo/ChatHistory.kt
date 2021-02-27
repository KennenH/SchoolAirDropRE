package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 消息记录表
 * 每一条记录就是一条消息
 */
@Entity(tableName = "offline")
data class ChatHistory(
        /**
         * 消息的指纹码
         */
        @PrimaryKey
        val fingerprint: String,

        /**
         * 发送方id
         *
         * 具体到消息的发送者id和接收方id可能是对方也可能是对方
         */
        val sender_id: String,

        /**
         * 接收方id 对于一个用户来说一定是自己的id
         *
         * 具体到消息的发送者id和接收方id可能是对方也可能是对方
         */
        val receiver_id: String,

        /**
         * 消息类型
         * 0 文本类型 可带emoji
         * 1 图片类型
         */
        val message_type: Int,

        /**
         * 消息内容
         */
        val message: String,

        /**
         * 发送时间
         */
        val send_time: Long,

        /**
         * 消息的状态
         * 0 接收正常或已发送，在代表发送时不一定成功，待IM框架回调后方可得知
         * 1 发送失败
         */
        var status: Int,
) {
    override fun toString(): String {
        return "ChatHistory(fingerprint='$fingerprint', sender_id='$sender_id', receiver_id='$receiver_id', message_type=$message_type, message='$message', send_time=$send_time, status=$status)"
    }
}

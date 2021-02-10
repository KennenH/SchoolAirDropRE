package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.schoolairdroprefactoredition.database.converter.DateConverter
import java.util.*

/**
 * 消息记录表
 * 每一条记录就是一条消息
 */
@TypeConverters(DateConverter::class)
@Entity(tableName = "chat_history")
data class ChatHistory(
        /**
         * 消息的指纹码
         */
        @PrimaryKey
        val fingerprint: String,

        /**
         * 发送方id
         */
        val sender_id: String,

        /**
         * 接收方id 对于一个用户来说一定是自己的id
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
        val send_time: Date,

        /**
         * 消息的状态
         * 0 接收正常或已发送，在代表发送时不一定成功，待IM框架回调后方可得知
         * 1 发送失败
         */
        val status: Int
)

package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity

/**
 * 消息数量列表
 * 仅有最后一条消息的指纹和发送人
 * 不要直接使用该表，通过[ChatOfflineNumDetail]查询详细信息
 */
@Entity(tableName = "offline", primaryKeys = ["sender_id", "receiver_id"])
data class ChatOfflineNum(

        val sender_id: String,

        val sender_name: String,

        val sender_avatar: String,

        val receiver_id: String,

        val unread_num: Int,

        val latest_fingerprint: String,

        /**
         * 是否显示在消息列表中
         *
         * 在消息列表中被侧滑删除之后被置为0
         * 当有新消息时置为1
         *
         * 1 显示
         * 0 不显示
         */
        val display: Int
)

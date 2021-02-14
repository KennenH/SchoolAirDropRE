package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity

/**
 * 消息会话列表
 * 仅有最后一条消息的指纹和对方基本信息
 * 不要直接使用该表，通过[ChatOfflineNumDetail]查询详细信息
 */
@Entity(tableName = "offline_num", primaryKeys = ["counterpart_id", "my_id"])
data class ChatOfflineNum(

        val counterpart_id: String,

        val my_id: String,

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
) {
        override fun toString(): String {
                return "ChatOfflineNum(counterpart_id='$counterpart_id', my_id='$my_id', unread_num=$unread_num, latest_fingerprint='$latest_fingerprint', display=$display)"
        }
}

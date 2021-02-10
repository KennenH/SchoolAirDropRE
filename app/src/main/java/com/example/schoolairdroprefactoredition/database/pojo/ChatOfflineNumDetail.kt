package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.DatabaseView
import androidx.room.TypeConverters
import com.example.schoolairdroprefactoredition.database.converter.DateConverter
import java.util.*

/**
 * 未读数量视图，在消息列表中才会使用该视图
 *
 * 查询返回时返回一个list数组，数组中的每一个元素都是来自一个用户的消息数
 * 只查询display为置1的聊天，display为0说明聊天被侧滑删除
 * 只有当有来自该用户的新消息时display才会被再置为1
 */
@TypeConverters(DateConverter::class)
@DatabaseView(viewName = "chat_offline_num_detail", value = "select offline.sender_id, offline.sender_name, offline.sender_avatar, offline.receiver_id, chat_history.message_type, chat_history.message, chat_history.send_time, offline.unread_num from offline, chat_history where offline.latest_fingerprint = chat_history.fingerprint and offline.sender_id = chat_history.sender_id and offline.receiver_id = chat_history.receiver_id and offline.display = 1 ")
data class ChatOfflineNumDetail(
        val sender_id: String,

        val sender_name: String,

        val sender_avatar: String,

        val receiver_id: String,

        val message_type: Int,

        val message: String,

        val send_time: Date,

        val unread_num: Int
)

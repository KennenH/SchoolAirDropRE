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
@DatabaseView(viewName = "offline_num_detail", value = "select offline_num.counterpart_id, user_info.user_name, user_info.user_avatar, offline_num.my_id, offline.message_type, offline.message, offline.send_time, offline_num.unread_num from offline_num, offline, user_info where offline_num.latest_fingerprint = offline.fingerprint and offline_num.display = 1 and offline_num.counterpart_id = user_info.user_id")
data class ChatOfflineNumDetail(

        val counterpart_id: String,

        val counterpart_name: String?,

        val counterpart_avatar: String?,

        val my_id: String,

        val message_type: Int,

        val message: String,

        val send_time: Date,

        val unread_num: Int
)

package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.DatabaseView

/**
 * 消息列表 详细（数据完整，可以直接用来显示）
 *
 * 查询返回时返回一个list数组，数组中的每一个元素都是来自一个用户的消息数
 * 只查询display为置1的聊天，display为0说明聊天被侧滑删除
 * 只有当有来自该用户的新消息时display才会被再置为1
 *
 * 2021/2/22  Bug fix: 此视图查询依赖于用户表，当对方是没有遇到过的用户，即本地没有该用户的缓存时，对方发来在线的第一条消息，将不会更新消息列表
 *            update: 在依赖user表的部分加上一个永远成立的语句，如此便可以在没有用户缓存的情况下查询出该会话
 *
 * 2021/2/23  Bug fix: 按照上一个bug修复方案，每有一个会话，查出的每个会话数量就会重复一遍
 *            update: 将消息列表的查询和用户信息的查询用左外连接分开，这样便是用户信息表为空也没关系
 */
@DatabaseView(viewName = "offline_num_detail", value = "select offline_num.counterpart_id, user_info.user_name , user_info.user_avatar, offline_num.my_id, offline.message_type, offline.message, offline.send_time, offline_num.unread_num from offline_num, offline left join user_info on user_id = offline.sender_id where offline_num.latest_fingerprint = offline.fingerprint and offline_num.display = 1 ")
data class ChatOfflineNumDetail(

        val counterpart_id: String,

        var counterpart_name: String?,

        var counterpart_avatar: String?,

        val my_id: String,

        val message_type: Int,

        val message: String,

        val send_time: Long,

        val unread_num: Int
)

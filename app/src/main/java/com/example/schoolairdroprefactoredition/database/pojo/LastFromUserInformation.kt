package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 记录来自该用户是否还有更多消息，若无则无需进行网络请求，反之当有更多离线消息，拉取后若获取的数量小于默认值，则
 * 将其置为false
 *
 * 在获取离线消息列表、拉取离线消息的时候均能将标识符置为true（获取的条数大于默认数）和false（获取的条数小于默认数）
 */
@Entity(tableName = "last_messages")
data class LastFromUserInformation(
        @PrimaryKey
        val user_id: String,

        /**
         * 标志来自改用户是否还有离线数据
         * 获取离线消息数量时来自大于10条消息的用户，就将这些来自这些用户的pull标志置为1
         *
         * 0 已无更多离线
         * 1 可能还有更多消息
         */
        val pull_flag: Boolean
)

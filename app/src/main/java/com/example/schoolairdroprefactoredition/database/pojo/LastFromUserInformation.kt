package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 记录已经接收并处理的最后一条消息，以及来自该用户是否还有更多消息
 */
@Entity(tableName = "last_messages")
data class LastFromUserInformation(
        @PrimaryKey
        val user_id: String,


        /**
         * ``已经接收`` 并 ``已处理`` 了的来自该用户的最后一条消息
         */
        val fingerprint: String,

        /**
         * 标志来自改用户是否还有离线数据
         * 获取离线消息数量时来自大于10条消息的用户，就将这些来自这些用户的pull标志置为1
         *
         * 0 已无更多离线
         * 1 可能还有更多消息
         */
        val pull_flag: Boolean
)

package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 记录对于特定的用户是否还需要进行离线消息的拉取和ack操作
 *
 * 在获取离线消息列表、拉取离线消息的时候均能将标识符置为true（获取的条数等于默认）和false（获取的条数不足默认）
 */
@Entity(tableName = "pull_flag")
data class PullFlag(
        @PrimaryKey
        val user_id: String,

        /**
         * 标志是否需要对于该用于请求网络离线或ack
         * 只有当来自一个用户的离线消息数量不足默认时才被置为false
         *
         * true  可以继续请求
         * false 无需再次请求，只有在返回的离线消息数据条数不足默认时才能被值为false
         */
        val pull_flag: Boolean
)

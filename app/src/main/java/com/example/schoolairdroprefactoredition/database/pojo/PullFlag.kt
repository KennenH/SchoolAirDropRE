package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 记录对于特定的用户是否还需要进行离线消息的拉取和ack操作
 *
 * 在获取离线消息列表、拉取离线消息的时候均能将标识符置为true（获取的条数不为0）和false（获取的条数等于0）
 */
@Entity(tableName = "pull_flag")
data class PullFlag(
        @PrimaryKey
        val user_id: String,

        /**
         * 标志是否需要对于该用于请求网络离线或ack
         * 只有当来自一个用户的离线消息为0时才被置为false，最后一次请求的任务仅仅是ack
         *
         * true  可以继续请求，继续请求的情况有两种
         * 1、上一次获取的离线消息条数等于[com.example.schoolairdroprefactoredition.utils.ConstantUtil.DATA_FETCH_DEFAULT_SIZE]
         *   则下一次请求的任务便是获取接下来默认条离线和ack上一批接受的消息
         * 2、上一次获取的离线消息条数小于[com.example.schoolairdroprefactoredition.utils.ConstantUtil.DATA_FETCH_DEFAULT_SIZE]
         *   则下一次请求的任务仅仅是去ack上一批获取到的消息，但是至关重要，否则用户永远无法ack小于默认值的那一批消息
         *
         * false 无需再次请求，只有在返回的离线消息数据条数等于0时才能被值为false
         */
        val pull_flag: Boolean
)

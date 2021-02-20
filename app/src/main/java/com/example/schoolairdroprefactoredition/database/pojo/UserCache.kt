package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserCache(
        @PrimaryKey
        val user_id: Int,

        val user_name: String,

        val user_avatar: String,

        val user_create_time: Long?,

        val user_goods_count: Int?,

        val user_post_count: Int?
//        val user_alipay: String,

        /**
         * 用户身份 0 普通用户 1 官方账号
         */
//        val user_status: Int,

//        val user_token: String,

//        /**
//         * 用于推送的设备id
//         */
//        val registration_id: String
) {
        override fun toString(): String {
                return "UserCache(user_id=$user_id, user_name='$user_name', user_avatar='$user_avatar', user_create_time=$user_create_time, user_goods_count=$user_goods_count, user_post_count=$user_post_count)"
        }
}
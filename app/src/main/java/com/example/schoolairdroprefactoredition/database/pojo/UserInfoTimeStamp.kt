package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.schoolairdroprefactoredition.database.converter.DateConverter
import java.util.*

/**
 * 用户信息修改的时间戳
 * 每次需要展示用户信息的时候都需要请求一次该用户的信息修改时间戳
 * 若本地时间戳不存在或者不一致则重新获取该用户的信息，否则直接从
 * 本地数据库中读取缓存显示
 */
@TypeConverters(DateConverter::class)
@Entity(tableName = "user_info_time_stamp")
data class UserInfoTimeStamp(
        @PrimaryKey
        val user_id: Int,

        val time_stamp: Date
)
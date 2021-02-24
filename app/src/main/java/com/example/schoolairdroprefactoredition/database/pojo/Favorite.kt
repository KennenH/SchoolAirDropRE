package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 本地收藏列表
 */
@Entity(tableName = "favorite")
data class Favorite(
        @PrimaryKey
        val goods_id: Int
) {
    override fun toString(): String {
        return "Favorite(goods_id=$goods_id)"
    }
}
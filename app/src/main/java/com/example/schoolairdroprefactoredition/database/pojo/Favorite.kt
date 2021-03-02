package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 本地收藏列表
 */
@Entity(tableName = "favorite")
data class Favorite(
        @PrimaryKey
        val goods_id: Int,

        val goods_name: String,

        val goods_cover_image: String,

        val goods_images: String,

        val goods_content: String,

        val goods_price: String,

        val goods_is_bargain: Boolean,

        val goods_is_secondHandel: Boolean,
){
        override fun toString(): String {
                return "Favorite(goods_id=$goods_id, goods_name='$goods_name', goods_cover_image='$goods_cover_image', goods_images='$goods_images', goods_content='$goods_content', goods_price='$goods_price', goods_is_bargain=$goods_is_bargain, goods_is_secondHandel=$goods_is_secondHandel)"
        }
}

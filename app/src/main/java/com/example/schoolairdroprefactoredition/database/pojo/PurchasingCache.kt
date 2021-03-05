package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 仅为首页的淘物进行缓存，进入app时先从该表中获取信息再获取网络信息
 */
@Entity(tableName = "purchasing")
data class PurchasingCache(
        @PrimaryKey
        val goods_id: Int,

        val goods_name: String,

        val goods_cover_image: String,

        val goods_is_bargain: Boolean,

        val goods_is_secondhand: Boolean,

        val goods_price: String,

        val user_name: String,

        val user_avatar: String
) {
    override fun toString(): String {
        return "PurchasingCache(goods_id=$goods_id, goods_name='$goods_name', goods_cover_image='$goods_cover_image', goods_is_bargain=$goods_is_bargain, goods_is_secondhand=$goods_is_secondhand, goods_price='$goods_price', user_name='$user_name', user_avatar='$user_avatar')"
    }
}

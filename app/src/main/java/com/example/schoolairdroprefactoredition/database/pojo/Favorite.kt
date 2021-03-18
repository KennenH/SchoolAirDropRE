package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 本地收藏列表
 */
@Entity(tableName = "favorite")
data class Favorite(
        @PrimaryKey
        val goods_id: Int,

        val user_id: Int,

        val goods_name: String,

        val goods_cover_image: String,

        val goods_price: String,

        val goods_is_bargain: Boolean,

        val goods_is_secondHand: Boolean,

        /**
         * 照理来说所有在收藏列表的物品都是被收藏的，但是在收藏Activity中为了方便用户重新将物品收藏回来，在取消
         * 收藏之后暂时将被取消收藏的物品保留，因此出现该标识符
         *
         * 在数据中，该值一定为true，只为收藏页面提供暂时改变物品收藏状态的功能
         */
        var goods_is_favor: Boolean
) {
    override fun toString(): String {
        return "Favorite(goods_id=$goods_id, user_id=$user_id, goods_name='$goods_name', goods_cover_image='$goods_cover_image', goods_price='$goods_price', goods_is_bargain=$goods_is_bargain, goods_is_secondHandel=$goods_is_secondHand)"
    }
}
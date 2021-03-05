package com.example.schoolairdroprefactoredition.domain

data class DomainGoodsAllDetailInfo(
        val code: Int,
        val `data`: Data,
        val msg: String,
        val time: String
) {
    data class Data(
            val goods_chat_count: Int,
            val goods_content: String,
            val goods_cover_image: String,
            val goods_favor_count: Int,
            val goods_id: Int,
            val goods_images: String,
            val goods_is_bargain: Boolean,
            val goods_is_secondHand: Boolean,
            val goods_latitude: Double,
            val goods_longitude: Double,
            val goods_name: String,
            val goods_price: Float,
            val goods_watch_count: Int,
            var goods_is_favored: Boolean,
            val seller: Seller
    ) {
        data class Seller(
                val last_login_time: String,
                val user_avatar: String,
                val user_contactCount: Int,
                val user_gender: String,
                val user_goodsOnSaleCount: Int,
                val user_id: Int,
                val user_name: String,
                val createtime: Long
        )
    }
}



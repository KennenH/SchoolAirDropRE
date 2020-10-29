package com.example.schoolairdroprefactoredition.domain

data class DomainOnGoing(
        val `data`: List<Data>,
        val success: Boolean
) {

    data class Data(
            val date: String,
            val event_id: Int,
            val goods_info: List<GoodsInfo>,
            val place: Any,
            val price: String,
            val sender_info: SenderInfo,
            val step: Int
    ) {

        data class GoodsInfo(
                val chat_count: Int,
                val favor_count: Int,
                val goods_description: String,
                val goods_id: Int,
                val goods_img_cover: String,
                val goods_img_set: String,
                val goods_is_brandNew: Int,
                val goods_is_quotable: Int,
                val goods_latitude: Double,
                val goods_longitude: Double,
                val goods_name: String,
                val goods_price: String,
                val goods_sellerid: Int,
                val watch_count: Int
        )

        data class SenderInfo(
                val credit_num: Int,
                val uid: Int,
                val uname: String,
                val user_img_path: String
        )
    }
}
package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainSelling(
        val code: Int,
        val `data`: List<Data>,
        val msg: String,
        val time: String
) : Serializable {
    data class Data(
            val goods_cover_image: String,
            val goods_id: Int,
            val goods_is_bargain: Boolean,
            val goods_is_secondHand: Boolean,
            val goods_latitude: Double,
            val goods_longitude: Double,
            val goods_name: String,
            val goods_price: Float,
            val goods_watch_count: Int
    ) : Serializable {
        override fun toString(): String {
            return "Data(goods_cover_image='$goods_cover_image', goods_id=$goods_id, goods_is_bargain=$goods_is_bargain, goods_is_secondHand=$goods_is_secondHand, goods_latitude=$goods_latitude, goods_longitude=$goods_longitude, goods_name='$goods_name', goods_price=$goods_price, goods_watch_count=$goods_watch_count)"
        }
    }

    override fun toString(): String {
        return "DomainSelling(code=$code, `data`=$`data`, msg='$msg', time='$time')"
    }
}


package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainOnGoing(
        val `data`: List<Data>,
        val success: Boolean
) : Serializable

data class Data(
        val buyer_info: BuyerInfo?,
        val date: String,
        val event_id: String,
        val goods_info: GoodsInfo,
        val place: Any,
        val price: String,
        val seller_info: SellerInfo?,
        val step: Int
) : Serializable

data class BuyerInfo(
        val credit_num: Int,
        val uid: Int,
        val uname: String,
        val user_img_path: String
) : Serializable

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
) : Serializable

data class SellerInfo(
        val credit_num: Int,
        val uid: Int,
        val uname: String,
        val user_img_path: String
) : Serializable
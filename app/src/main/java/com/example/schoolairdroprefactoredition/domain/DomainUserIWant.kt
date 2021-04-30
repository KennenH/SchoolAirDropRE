package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainUserIWant(
        val code: Int,
        val `data`: List<Data>,
        val msg: Any,
        val time: String
) : Serializable {

    data class Data(
            val createtime: Int,
            val iwant_color: Int,
            val iwant_content: String,
            val iwant_id: Int,
            val iwant_images: String,
            val iwant_latitude: Double,
            val iwant_longitude: Double,
            val seller: Seller,
            val tag: Tag
    ) : Serializable {

        data class Seller(
                val last_login_time: String,
                val user_avatar: String,
                val user_contactCount: Int,
                val user_goodsOnSaleCount: Int,
                val user_id: Int,
                val user_name: String) : Serializable

        data class Tag(
                val tags_content: String,
                val tags_id: Int
        ) : Serializable
    }
}


package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

/**
 * @author kennen
 * @date 2021/4/18
 */
data class DomainIWant(
        val code: Int,
        val `data`: List<Data>,
        val msg: Any,
) : Serializable {

    data class Data(
            val iwant_id: Int,
            val iwant_color: Int,
            val iwant_content: String,
            val iwant_images: String,
            val seller: Seller,
            val tag: String
    ) : Serializable {
        data class Seller(
                val last_login_time: String,
                val user_avatar: String,
                val user_contactCount: Int,
                val user_goodsOnSaleCount: Int,
                val user_id: Int,
                val user_name: String
        ) : Serializable {
            override fun toString(): String {
                return "Seller(last_login_time='$last_login_time', user_avatar='$user_avatar', user_contactCount=$user_contactCount, user_goodsOnSaleCount=$user_goodsOnSaleCount, user_id=$user_id, user_name='$user_name')"
            }
        }

        override fun toString(): String {
            return "Data(iwant_color=$iwant_color, iwant_content='$iwant_content', iwant_id=$iwant_id, iwant_images='$iwant_images', seller=$seller, tag='$tag')"
        }
    }

    override fun toString(): String {
        return "DomainIWant(code=$code, `data`=$`data`, msg=$msg)"
    }

}

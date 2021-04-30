package com.example.schoolairdroprefactoredition.domain

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import java.io.Serializable

/**
 * @author kennen
 * @date 2021/4/18
 */
data class DomainIWant(
        val code: Int = 400,
        val `data`: List<Data>,
        val msg: String? = null,
) : Serializable {

    data class Data(
            val iwant_id: Int,
            val iwant_color: Int,
            val iwant_content: String,
            val iwant_images: String,
            val seller: Seller,
            val tag: String,

            override val itemType: Int = IWantRecyclerAdapter.TYPE_ONE,
    ) : Serializable, MultiItemEntity {
        data class Seller(
                val last_login_time: String? = null,
                val user_avatar: String,
                val user_contactCount: Int? = null,
                val user_goodsOnSaleCount: Int? = null,
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

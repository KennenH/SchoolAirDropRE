package com.example.schoolairdroprefactoredition.database.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author kennen
 * @date 2021/4/27
 *
 * 仅为首页的求购进行缓存，进入app时先从该表中获取信息再获取网络信息
 */
@Entity(tableName = "iwant")
data class IWantCache(
        @PrimaryKey
        val iwant_id: Int,

        val iwant_content: String,

        val iwant_images: String,

        val iwant_card_color: Int,

        val iwant_tag: String,

        val user_id: Int,

        val user_name: String,

        val user_avatar: String
) {
    override fun toString(): String {
        return "IWantCache(iwant_id=$iwant_id, iwant_content='$iwant_content', iwant_images='$iwant_images', iwant_card_color=$iwant_card_color, iwant_tag='$iwant_tag', user_id=$user_id, user_name='$user_name', user_avatar='$user_avatar')"
    }
}
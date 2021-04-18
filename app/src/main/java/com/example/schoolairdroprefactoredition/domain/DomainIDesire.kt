package com.example.schoolairdroprefactoredition.domain

/**
 * @author kennen
 * @date 2021/4/18
 */
data class DomainIDesire(
        val code: Int,
        val msg: String,
        val data: Data
) {

    data class Data(
            val inquiry_id: Int,
            val inquiry_content: String,
            val inquiry_tag_id: Int,
            val inquiry_images: String,
            val inquiry_longitude: Double,
            val inquiry_latitude: Double,
            val inquiry_owner_id: Int,
            val inquiry_owner_name: String,
            val inquiry_owner_avatar: String) {
        override fun toString(): String {
            return "Data(inquiry_id=$inquiry_id, inquiry_content='$inquiry_content', inquiry_tag_id=$inquiry_tag_id, inquiry_images='$inquiry_images', inquiry_longitude=$inquiry_longitude, inquiry_latitude=$inquiry_latitude, inquiry_owner_id=$inquiry_owner_id, inquiry_owner_name='$inquiry_owner_name', inquiry_owner_avatar='$inquiry_owner_avatar')"
        }
    }

    override fun toString(): String {
        return "DomainInquiry(code=$code, msg='$msg', data=$data)"
    }
}
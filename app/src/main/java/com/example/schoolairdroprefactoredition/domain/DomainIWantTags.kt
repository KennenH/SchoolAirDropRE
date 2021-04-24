package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainIWantTags(
        val code: Int,
        val msg: String,
        val data: List<Data>
) : Serializable {
    data class Data(
            val tag_id: Int,
            val tag: String
    ) : Serializable {
        override fun toString(): String {
            return "Data(tag_id=$tag_id, tag='$tag')"
        }
    }

    override fun toString(): String {
        return "DomainIWantTags(code=$code, msg='$msg', data=$data)"
    }

}

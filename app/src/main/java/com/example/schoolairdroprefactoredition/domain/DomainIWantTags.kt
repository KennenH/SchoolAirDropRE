package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainIWantTags(
        val code: Int,
        val `data`: List<Data>,
        val msg: String,
) : Serializable {

    data class Data(
            val tags_id: Int,
            val tags_content: String,
    ) : Serializable {
        override fun toString(): String {
            return "Data(tags_content='$tags_content', tags_id=$tags_id)"
        }
    }

    override fun toString(): String {
        return "DomainIWantTags(code=$code, `data`=$`data`, msg='$msg')"
    }
}


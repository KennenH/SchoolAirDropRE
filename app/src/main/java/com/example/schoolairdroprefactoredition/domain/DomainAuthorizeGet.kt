package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainAuthorizeGet(
        val code: Int,
        val msg: String,
        val `data`: Data,
) : Serializable {

    data class Data(
            val public_key: String
    )
}
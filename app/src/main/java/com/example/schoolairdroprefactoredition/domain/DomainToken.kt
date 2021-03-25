package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainToken(
        val code: Int,
        val `data`: Data,
        val msg: String
) : Serializable {

    val access_token
        get() = data.access_token

    data class Data(
            val access_token: String,
            /**
             * 单位秒
             */
            val expires_in: Long,
            val token_type: String,
            val scope: String) : Serializable

    override fun toString(): String {
        return "DomainToken(code=$code, `data`=$`data`, msg='$msg', access_token='$access_token')"
    }
}


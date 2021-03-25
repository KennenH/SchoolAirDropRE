package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainToken

data class UserTokenCache(
        val token: DomainToken
) {
    companion object {
        const val KEY = "hahahahaa"
    }

    override fun toString(): String {
        return "UserTokenCache(token=$token)"
    }


}

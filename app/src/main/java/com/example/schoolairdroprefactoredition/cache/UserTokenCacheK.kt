package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainToken

data class UserTokenCacheK(val token: DomainToken) {
    companion object {
        const val USER_TOKEN = "imatoken"
    }
}

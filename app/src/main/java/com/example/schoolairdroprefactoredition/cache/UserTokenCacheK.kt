package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize

data class UserTokenCacheK(val token: DomainAuthorize) {
    companion object {
        const val USER_TOKEN = "imatoken"
    }
}

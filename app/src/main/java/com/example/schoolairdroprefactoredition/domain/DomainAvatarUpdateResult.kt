package com.example.schoolairdroprefactoredition.domain

data class DomainAvatarUpdateResult(
        val code: Int,
        val msg: String,
        var `data`: Data
) {
    data class Data(
            var avatar_url: String
    )
}
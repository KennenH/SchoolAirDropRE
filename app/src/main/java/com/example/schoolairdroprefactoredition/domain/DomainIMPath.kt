package com.example.schoolairdroprefactoredition.domain

data class DomainIMPath(
        val code: Int,
        val `data`: Data,
        val msg: String,
) {
    data class Data(
            val path_url: List<String>
    )
}


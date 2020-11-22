package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainTrashBin(
        val `data`: List<BinData>,
        val success: Boolean
) : Serializable

data class BinData(
        val date: String,
        val event_id: String,
        val place: Any,
        val price: String,
        val step: Int
) : Serializable
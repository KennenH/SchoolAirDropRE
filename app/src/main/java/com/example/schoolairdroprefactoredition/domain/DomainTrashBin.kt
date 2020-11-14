package com.example.schoolairdroprefactoredition.domain

import java.io.Serializable

data class DomainTrashBin(
        val `data`: List<Data>,
        val success: Boolean
) : Serializable

package com.example.schoolairdroprefactoredition.domain

import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import java.io.Serializable

data class DomainResult(
        val code: Int,
        val msg: String) : Serializable {

    val isSuccess
        get() = code == ConstantUtil.HTTP_OK
}
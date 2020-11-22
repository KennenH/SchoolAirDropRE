package com.example.schoolairdroprefactoredition.domain.base

/**
 * 标识加载状态的枚举类
 */
enum class LoadState {
    SUCCESS,
    LOADING,
    ERROR,
    EMPTY,
    AUTHORIZATION_OUT_OF_DATE,
}
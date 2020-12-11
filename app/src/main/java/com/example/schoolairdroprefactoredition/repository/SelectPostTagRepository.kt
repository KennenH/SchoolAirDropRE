package com.example.schoolairdroprefactoredition.repository

class SelectPostTagRepository private constructor() {
    companion object {
        private var INSTANCE: SelectPostTagRepository? = null
        fun getInstance() = INSTANCE
                ?: SelectPostTagRepository().also {
                    INSTANCE = it
                }
    }


}
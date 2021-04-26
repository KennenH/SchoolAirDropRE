package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantTagRepository private constructor() {

    companion object {
        private var INSTANCE: IWantTagRepository? = null
        fun getInstance() = INSTANCE
                ?: IWantTagRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取所有求购标签
     */
    fun getIWantTags(onResult: (DomainIWantTags?) -> Unit) {
        RetrofitClient.iWantApi.getIWantTags(
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }
}
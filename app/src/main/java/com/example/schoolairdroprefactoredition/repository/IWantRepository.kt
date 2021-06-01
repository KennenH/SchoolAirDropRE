package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantRepository {

    companion object {
        private var INSTANCE: IWantRepository? = null
        fun getInstance() = INSTANCE
                ?: IWantRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取附近求购
     */
    fun getNearByIWant(
            page: Int,
            longitude: Double, latitude: Double,
            onResult: (DomainIWant?) -> Unit) {
        RetrofitClient.iWantApi.getNearByIWant(
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
                page).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }
}
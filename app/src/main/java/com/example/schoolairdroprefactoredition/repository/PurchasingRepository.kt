package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class PurchasingRepository private constructor() {

    companion object {
        private var INSTANCE: PurchasingRepository? = null
        fun getInstance() = INSTANCE
                ?: PurchasingRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 请求附近在售的物品信息
     */
    fun getNearbyGoods(page: Int, longitude: Double, latitude: Double, onResult: (response: DomainPurchasing?) -> Unit) {
        RetrofitClient.goodsApi.getNearByGoods(
                ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, page,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude)
                .apply {
                    enqueue(object : CallBackWithRetry<DomainPurchasing>(this@apply) {
                        override fun onResponse(call: Call<DomainPurchasing>, response: Response<DomainPurchasing>) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                val body = response.body()
                                if (body != null && body.code == ConstantUtil.HTTP_OK) {
                                    onResult(body)
                                } else {
                                    onResult(null)
                                }
                            } else {
                                onResult(null)
                            }
                        }

                        override fun onFailureAllRetries() {
                            onResult(null)
                        }
                    })
                }
    }
}
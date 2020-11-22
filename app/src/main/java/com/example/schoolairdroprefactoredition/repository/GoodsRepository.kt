package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet
import com.example.schoolairdroprefactoredition.domain.DomainQuote
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry
import com.example.schoolairdroprefactoredition.model.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class GoodsRepository private constructor() {

    companion object {
        private var INSTANCE: GoodsRepository? = null
        fun getInstance() = INSTANCE
                ?: GoodsRepository().also {
                    INSTANCE = it
                }
    }

    fun quoteItem(token: String,
                  goodsID: String,
                  quotePrice: String,
                  onResult: (success: Boolean, response: DomainResult?) -> Unit) {
        RetrofitClient.goodsApi.quoteRequest(token, goodsID, quotePrice).apply {
            enqueue(object : CallBackWithRetry<DomainResult>(this) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val result = response.body()
                        if (response.isSuccessful && result != null) {
                            onResult(true, result)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }
            })
        }
    }
}
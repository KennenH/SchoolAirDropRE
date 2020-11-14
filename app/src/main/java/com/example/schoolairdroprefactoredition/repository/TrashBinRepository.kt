package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.domain.DomainTrashBin
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry
import com.example.schoolairdroprefactoredition.model.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class TrashBinRepository private constructor() {

    companion object {
        private var instance: TrashBinRepository? = null
        fun getInstance() = instance
                ?: TrashBinRepository().also {
                    instance = it
                }
    }

    /**
     * 获取垃圾箱
     */
    fun getTrashBin(token: String,
                    onResult: (success: Boolean, response: DomainTrashBin?) -> Unit) {
        RetrofitClient.goodsApi.getTrashBin(token).apply {
            enqueue(object : CallBackWithRetry<DomainTrashBin>(this) {
                override fun onResponse(call: Call<DomainTrashBin>, response: Response<DomainTrashBin>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val trashBin = response.body()
                        if (trashBin != null && trashBin.success) {
                            onResult(true, trashBin)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailureAllRetries() {
                    onResult(false, null)
                }
            })
        }
    }
}
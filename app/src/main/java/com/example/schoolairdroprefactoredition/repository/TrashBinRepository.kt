package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.domain.BinData
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
     * 获取被中断的交易记录
     */
    fun getCorrupted(token: String,
                     onResult: (success: Boolean, response: DomainTrashBin?) -> Unit) {
        val list = arrayListOf<BinData>()
        for (i in 0 until 5) {
            val bin = BinData("", "1", "中国计量大学", "21.3", 1)
            list.add(bin)
        }
        onResult(true, DomainTrashBin(list, true))
//        RetrofitClient.goodsApi.getCorrupted(token).apply {
//            enqueue(object : CallBackWithRetry<DomainTrashBin>(this) {
//                override fun onResponse(call: Call<DomainTrashBin>, response: Response<DomainTrashBin>) {
//                    if (response.code() == HttpURLConnection.HTTP_OK) {
//                        val trashBin = response.body()
//                        if (trashBin != null && trashBin.success) {
//                            onResult(true, trashBin)
//                        } else {
//                            onResult(false, null)
//                        }
//                    } else {
//                        onResult(false, null)
//                    }
//                }
//
//                override fun onFailureAllRetries() {
//                    onResult(false, null)
//                }
//            })
//        }
    }

    /**
     * 获取已完成的交易记录
     */
    fun getAccomplished(token: String,
                        onResult: (success: Boolean, response: DomainTrashBin?) -> Unit) {
        val list = arrayListOf<BinData>()
        for (i in 0 until 5) {
            val bin = BinData("", "1", "中国计量大学", "21.3", 1)
            list.add(bin)
        }
        onResult(true, DomainTrashBin(list, true))
//        RetrofitClient.goodsApi.getAccomplished(token).apply {
//            enqueue(object : CallBackWithRetry<DomainTrashBin>(this) {
//                override fun onResponse(call: Call<DomainTrashBin>, response: Response<DomainTrashBin>) {
//                    if (response.code() == HttpURLConnection.HTTP_OK) {
//                        val trashBin = response.body()
//                        if (trashBin != null && trashBin.success) {
//                            onResult(true, trashBin)
//                        } else {
//                            onResult(false, null)
//                        }
//                    } else {
//                        onResult(false, null)
//                    }
//                }
//
//                override fun onFailureAllRetries() {
//                    onResult(false, null)
//                }
//            })
//        }
    }
}
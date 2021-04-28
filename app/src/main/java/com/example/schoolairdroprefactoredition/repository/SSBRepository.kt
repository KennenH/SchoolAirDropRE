package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class SSBRepository {

    companion object {
        private var INSTANCE: SSBRepository? = null
        fun getInstance() = INSTANCE
                ?: SSBRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 用户id获取用户在售物品
     */
    fun getSelling(userID: Int, onResult: (DomainSelling?) -> Unit) {
        RetrofitClient.goodsApi.getGoodsOnSaleByClient(
                userID,
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 下架物品
     */
    fun deleteGoods(token: String, goodsID: String, onResult: (Boolean) -> Unit) {
        RetrofitClient.goodsApi.deleteGoods(token, goodsID.toInt()).apply {
            enqueue(object : CallbackWithRetry<DomainResult>(this@apply) {
                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                    onResult(response.code() == HttpURLConnection.HTTP_OK)
                }

                override fun onFailureAllRetries() {
                    onResult(false)
                }
            })
        }
    }

    /**
     * 用户id获取用户求购
     */
    fun getIWant(
            userID: Int,
            page: Int,
            onResult: (DomainIWant?) -> Unit) {
        RetrofitClient.iWantApi.getIWantByUserID(
                userID, page,
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 删除求购
     */
    fun deleteIWant(
            token: String,
            iwantID: String,
            onResult: (Boolean) -> Unit) {
        RetrofitClient.iWantApi.deteleIWant(token, iwantID).apply {
            enqueue(object : CallbackWithRetry<DomainResult>(this@apply) {
                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                    onResult(response.code() == HttpURLConnection.HTTP_OK)
                }

                override fun onFailureAllRetries() {
                    onResult(false)
                }
            })
        }
    }
}
package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class SellingRepository {

    companion object {
        private var INSTANCE: SellingRepository? = null
        fun getInstance() = INSTANCE
                ?: SellingRepository().also {
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
            enqueue(object : CallBackWithRetry<DomainSelling>(this@apply) {
                override fun onResponse(call: Call<DomainSelling>, response: Response<DomainSelling>) {
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

    /**
     * 下架物品
     */
    fun deleteGoods(token: String, goodsID: String, onResult: (Boolean) -> Unit) {
        RetrofitClient.goodsApi.deleteGoods(token, goodsID.toInt()).apply {
            enqueue(object : CallBackWithRetry<DomainResult>(this@apply) {
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
     * 用户id获取用户帖子
     */
    fun getPosts(token: String, page: Int) {}

    /**
     * 删除帖子
     */
    fun deletePost(token: String, postID: String) {}
}
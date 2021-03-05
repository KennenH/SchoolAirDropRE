package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
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

    /**
     * 获取物品全部详细信息
     *
     * @param onResult
     * null 信息获取错误
     * 非空 code == 200 物品信息获取成功
     * 非空 code == 404 物品已被下架
     */
    fun getGoodsAllDetail(goodsID: Int,
                          onResult: (response: DomainGoodsAllDetailInfo?) -> Unit) {
        RetrofitClient.goodsApi.getGoodsAllDetail(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, goodsID).apply {
            enqueue(object : CallBackWithRetry<DomainGoodsAllDetailInfo>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainGoodsAllDetailInfo>, response: Response<DomainGoodsAllDetailInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        onResult(body)
                    } else {
                        onResult(null)
                    }
                }
            })
        }
    }

    /**
     * 获取物品剩下的信息
     *
     * @param onResult
     * response code 200 -- > 物品信息获取成功
     * response code 404 -- > 物品已下架
     * response 空 -- > 信息获取失败
     */
    fun getGoodsDetail(goodsID: Int,
                       onResult: (response: GoodsDetailInfo?) -> Unit) {
        RetrofitClient.goodsApi.getGoodsDetail(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, goodsID).apply {
            enqueue(object : CallBackWithRetry<GoodsDetailInfo>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<GoodsDetailInfo>, response: Response<GoodsDetailInfo>) {
                    val result = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful && result != null) {
                            onResult(result)
                        } else {
                            onResult(null)
                        }
                    } else {
                        if (result?.code == HttpURLConnection.HTTP_NOT_FOUND) {
                            onResult(result)
                        } else {
                            onResult(null)
                        }
                    }
                }
            })
        }
    }
}
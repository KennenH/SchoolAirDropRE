package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.domain.DomainResult
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
     * 收藏或取消收藏
     * @param isFavor 该物品是否已被收藏，true则执行取消收藏，false则执行收藏
     */
    fun toggleFavorGoods(token: String, goodsID: Int, isFavor: Boolean, onResult: (Boolean) -> Unit) {
        if (isFavor) {
            RetrofitClient.goodsApi.unFavorGoods(token, goodsID).apply {
                enqueue(object : CallbackWithRetry<DomainResult>(this@apply) {
                    override fun onFailureAllRetries() {
                        onResult(false)
                    }

                    override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val body = response.body()
                            if (body != null && body.isSuccess) {
                                onResult(true)
                            } else {
                                onResult(false)
                            }
                        } else {
                            onResult(false)
                        }
                    }
                })
            }
        } else {
            RetrofitClient.goodsApi.favorGoods(token, goodsID).apply {
                enqueue(object : CallbackWithRetry<DomainResult>(this@apply) {
                    override fun onFailureAllRetries() {
                        onResult(false)
                    }

                    override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val body = response.body()
                            if (body != null && body.isSuccess) {
                                onResult(true)
                            } else {
                                onResult(false)
                            }
                        } else {
                            onResult(false)
                        }
                    }
                })
            }
        }
    }

    /**
     * 当前账号浏览一个物品每24小时仅为该物品添加一个浏览量
     */
    fun browseGoods(goodsID: Int) {
        RetrofitClient.goodsApi.browseGoods(
                goodsID,
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET).apply {
            enqueue(object : CallbackWithRetry<DomainResult>(this@apply) {
                override fun onFailureAllRetries() {
                }

                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                }
            })
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
            enqueue(object : CallbackWithRetry<DomainGoodsAllDetailInfo>(this@apply) {
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
}
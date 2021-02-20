package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.qiniu.android.utils.LogUtil
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
     * 获取物品剩下的信息
     */
    fun getGoodsDetail(goodsID: Int,
                       onResult: (success: Boolean, response: GoodsDetailInfo?) -> Unit) {
        RetrofitClient.goodsApi.getGoodsDetail(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, goodsID).apply {
            enqueue(object : CallBackWithRetry<GoodsDetailInfo>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<GoodsDetailInfo>, response: Response<GoodsDetailInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val result = response.body()
                        if (response.isSuccessful && result != null) {
                            onResult(true, result)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        LogUtil.d(response.errorBody()?.string())
                        onResult(false, null)
                    }
                }
            })
        }
    }
}
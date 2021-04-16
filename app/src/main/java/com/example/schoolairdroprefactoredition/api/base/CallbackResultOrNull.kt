package com.example.schoolairdroprefactoredition.api.base

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection


/**
 * @author kennen
 * @date 2021/4/6
 */
open class CallbackResultOrNull<T>(call: Call<T>, private val onResult: (response: T?) -> Unit) : CallbackWithRetry<T>(call) {

    override fun onFailureAllRetries() {
        onResult(null)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        when (response.code()) {
            // 请求成功
            HttpURLConnection.HTTP_OK -> {
                onResult(response.body())
            }

            // refresh token已失效
            ConstantUtil.HTTP_INVALID_REFRESH_TOKEN -> {

            }

            else -> {
                LogUtils.d(response.errorBody()?.string())
                onResult(null)
            }
        }
    }
}
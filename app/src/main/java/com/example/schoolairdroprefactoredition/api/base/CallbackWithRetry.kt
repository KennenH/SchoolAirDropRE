package com.example.schoolairdroprefactoredition.api.base

import com.blankj.utilcode.util.LogUtils
import retrofit2.Call
import retrofit2.Callback

abstract class CallbackWithRetry<T>(private val call: Call<T>) : Callback<T> {

    companion object {
        /**
         * 最多自动重试2次
         */
        private const val TOTAL_RETRIES = 2
    }

    private var retryCount = 0

    override fun onFailure(call: Call<T>, t: Throwable) {
        call.cancel()
        if (retryCount++ < TOTAL_RETRIES) {
            call.clone().enqueue(this)
        } else {
            LogUtils.d(t.toString())
            onFailureAllRetries()
        }
    }

    /**
     * 所有重试均失败则视为失败
     */
    abstract fun onFailureAllRetries()
}
package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.utils.JsonCacheConstantUtil
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class UserRepository private constructor() {

    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance() = INSTANCE
                ?: UserRepository().also {
                    INSTANCE = it
                }
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * user id获取用户信息
     */
    fun getUserInfoById(userID: Int, onResult: (success: Boolean, response: DomainUserInfo.DataBean?) -> Unit) {
        RetrofitClient.userApi.getUserInfoByID(userID).apply {
            enqueue(object : CallBackWithRetry<DomainUserInfo>(this@apply) {
                override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {

                    LogUtils.d(response.errorBody()?.string())

                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful) {
                            // 保存再次请求的限制，防止频繁调用请求
                            jsonCacheUtil.saveCache(JsonCacheConstantUtil.IS_GET_USER_INFO_PRESENTLY + userID, true, JsonCacheConstantUtil.NEXT_GET_TIME_SPAN)
                            val body = response.body()
                            onResult(true, body?.data)
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
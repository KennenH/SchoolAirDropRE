package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
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

    /**
     * user id获取用户信息
     */
    fun getUserInfoById(userID: Int, onResult: (success: Boolean, response: DomainUserInfo.DataBean?) -> Unit) {
        RetrofitClient.userApi.getUserInfoByID(userID).apply {
            enqueue(object : CallbackWithRetry<DomainUserInfo>(this@apply) {
                override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful) {
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
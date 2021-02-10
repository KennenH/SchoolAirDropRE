package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainResult
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class UserNameRepository private constructor() {

    companion object {
        private var INSTANCE: UserNameRepository? = null
        fun getInstance() = INSTANCE
                ?: UserNameRepository().also {
                    INSTANCE = it
                }
    }

    fun rename(token: String, name: String, onResult: (success: Boolean) -> Unit) {
        RetrofitClient.userApi.updateUserName(token, name).apply {
            enqueue(object : CallBackWithRetry<DomainResult>(this@apply) {
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

                override fun onFailureAllRetries() {
                    onResult(false)
                }
            })
        }
    }
}
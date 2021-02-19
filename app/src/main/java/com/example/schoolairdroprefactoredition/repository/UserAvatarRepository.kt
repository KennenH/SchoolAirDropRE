package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainResult
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class UserAvatarRepository {

    companion object {
        private var INSTANCE: UserAvatarRepository? = null
        fun getInstance() = INSTANCE
                ?: UserAvatarRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 修改服务器上头像的路径和全名
     */
    fun updateAvatar(token: String, avatarRemotePath: String, onResult: (Boolean) -> Unit) {
        RetrofitClient.userApi.updateUserAvatar(token, avatarRemotePath).apply {
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
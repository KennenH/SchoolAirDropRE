package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
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
    fun updateAvatar(token: String, taskID: String, avatar: String, onResult: (String?) -> Unit) {
        RetrofitClient.userApi.updateUserAvatar(token, taskID, avatar).apply {
            enqueue(object : CallbackWithRetry<DomainAvatarUpdateResult>(this@apply) {
                override fun onResponse(call: Call<DomainAvatarUpdateResult>, response: Response<DomainAvatarUpdateResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.code == ConstantUtil.HTTP_OK) {
                            onResult(body.data.avatar_url)
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


}
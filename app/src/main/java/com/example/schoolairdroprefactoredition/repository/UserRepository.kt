package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.domain.DomainBaseUser
import com.example.schoolairdroprefactoredition.domain.DomainBaseUserInfo
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import javadz.beanutils.BeanUtils
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

    fun getUserInfo(userID: Int, onResult: (success: Boolean, response: DomainBaseUserInfo?) -> Unit) {
        RetrofitClient.userApi.getUserInfoByID(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, userID).apply {
            enqueue(object : CallBackWithRetry<DomainBaseUser>(this@apply) {
                override fun onResponse(call: Call<DomainBaseUser>, response: Response<DomainBaseUser>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            val baseUserInfo = DomainBaseUserInfo()
                            BeanUtils.copyProperties(baseUserInfo, body?.data?.get(0))
                            onResult(true, baseUserInfo)
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
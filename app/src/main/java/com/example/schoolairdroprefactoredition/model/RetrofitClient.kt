package com.example.schoolairdroprefactoredition.model

import com.example.schoolairdroprefactoredition.model.api.GoodsApi
import com.example.schoolairdroprefactoredition.model.api.UserApi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val dispatcher: Dispatcher = Dispatcher()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .dispatcher(dispatcher.also { dispatcher.maxRequests = 1 })
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(UserApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    /**
     * 用户接口
     */
    val userApi: UserApi = retrofit.create(UserApi::class.java)

    /**
     * 物品接口
     */
    val goodsApi: GoodsApi = retrofit.create(GoodsApi::class.java)
}
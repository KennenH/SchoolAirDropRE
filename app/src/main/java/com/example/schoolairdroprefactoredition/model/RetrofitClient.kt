package com.example.schoolairdroprefactoredition.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(4, TimeUnit.SECONDS)
            .connectTimeout(4, TimeUnit.SECONDS)
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
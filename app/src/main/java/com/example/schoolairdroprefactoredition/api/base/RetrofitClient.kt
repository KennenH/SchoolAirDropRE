package com.example.schoolairdroprefactoredition.api.base

import com.example.schoolairdroprefactoredition.api.GoodsApi
import com.example.schoolairdroprefactoredition.api.PostApi
import com.example.schoolairdroprefactoredition.api.UserApi
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DemoConstantUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

//    private val dispatcher: Dispatcher = Dispatcher()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(2500, TimeUnit.MILLISECONDS)
//            .dispatcher(dispatcher.also { dispatcher.maxRequests = 1 })
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    val demoRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(DemoConstantUtil.DEMO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    /**
     * 用户接口
     */
    val userApi: UserApi = retrofit.create(UserApi::class.java)

    val demoUserApi: UserApi = demoRetrofit.create(UserApi::class.java)

    /**
     * 物品接口
     */
    val goodsApi: GoodsApi = retrofit.create(GoodsApi::class.java)


    val demoGoodsApi: GoodsApi = demoRetrofit.create(GoodsApi::class.java)

    /**
     * 帖子接口
     */
    val postApi: PostApi = retrofit.create(PostApi::class.java)
}
package com.example.schoolairdroprefactoredition.api.base

import com.example.schoolairdroprefactoredition.api.*
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .cookieJar(object : CookieJar {
                    private val cookieStore = HashMap<String, List<Cookie>>()

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        val cookies = cookieStore[url.host]
                        return cookies ?: arrayListOf()
                    }

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore[url.host] = cookies
                    }
                })
                .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(ConstantUtil.SCHOOL_AIRDROP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    private val retrofitIM by lazy {
        Retrofit.Builder()
                .baseUrl(ConstantUtil.SCHOOL_AIRDROP_BASE_URL_IM)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    /**
     * 用户接口
     */
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    /**
     * 物品接口
     */
    val goodsApi: GoodsApi by lazy {
        retrofit.create(GoodsApi::class.java)
    }

    /**
     * 帖子接口
     */
    val inquiryApi: InquiryApi by lazy {
        retrofit.create(InquiryApi::class.java)
    }

    /**
     * IM接口
     */
    val imApi: IMApi by lazy {
        retrofitIM.create(IMApi::class.java)
    }

    /**
     * 图片上传独立接口
     */
    val uploadApi: UploadApi by lazy {
        retrofit.create(UploadApi::class.java)
    }
}
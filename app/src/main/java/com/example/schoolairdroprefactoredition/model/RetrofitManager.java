package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final RetrofitManager instance = new RetrofitManager();
    private final Retrofit mRetrofit;// final 变量可以在构造器中初始化

    public static RetrofitManager getInstance() {
        return instance;
    }

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW)
                .client(new OkHttpClient.Builder()
                        .readTimeout(3, TimeUnit.SECONDS)
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}

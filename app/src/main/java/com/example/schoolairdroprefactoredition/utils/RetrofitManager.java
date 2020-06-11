package com.example.schoolairdroprefactoredition.utils;

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
                .baseUrl(ConstantUtil.SOB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}

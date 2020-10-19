package com.example.schoolairdroprefactoredition.model;

import com.blankj.utilcode.util.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class CallBackWithRetry<T> implements Callback<T> {

    private static final int TOTAL_RETRIES = 3;
    private final Call<T> call;
    private int retryCount = 0;

    private boolean isRequesting = false; // 正在请求标志，当正在请求时拦截相同的请求

    public CallBackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retryCount++ < TOTAL_RETRIES) {
            retry();
        } else {
            onFailureAllRetries();
            LogUtils.d(t.toString());
        }
    }

    /**
     * 所有重试均失败则视为失败
     */
    public abstract void onFailureAllRetries();

    private void retry() {
        call.clone().enqueue(this);
    }
}
package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IGoodsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodsImpl implements IGoodsPresenter {
    private IGoodsCallback mCallback;

    @Override
    public void quoteRequest(String token, String goodsID, String quotePrice) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.quoteRequest(token, goodsID, quotePrice);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (mCallback != null)
                        if (result != null && result.isSuccess())
                            mCallback.onQuoteSuccess();
                        else
                            mCallback.onError();
                }
            }
        });
    }

    @Override
    public void favorite(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.favorite(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (result != null && result.isSuccess())
                        mCallback.onQuoteSuccess();
                    else
                        mCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerCallback(IGoodsCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IGoodsCallback callback) {
        mCallback = null;
    }
}

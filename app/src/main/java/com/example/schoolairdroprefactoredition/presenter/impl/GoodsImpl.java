package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IGoodsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodsImpl implements IGoodsPresenter {
    private IGoodsCallback mCallback;

    @Override
    public void quoteRequest(String token, String goodsID, String quotePrice) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.quoteRequest(token, goodsID, quotePrice);
        task.enqueue(new Callback<DomainResult>() {
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

            @Override
            public void onFailure(Call<DomainResult> call, Throwable t) {
                mCallback.onError();
                LogUtils.d(t.toString());
            }
        });
    }

    @Override
    public void favorite(String token, int goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);

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

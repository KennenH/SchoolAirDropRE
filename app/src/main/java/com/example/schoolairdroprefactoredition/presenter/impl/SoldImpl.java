package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISoldPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SoldImpl implements ISoldPresenter {
    private ISoldCallback mCallback;

    @Override
    public void getSoldList(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getMySoldGoods(token);
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(@NotNull Call<DomainGoodsInfo> call, @NotNull Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();
                    if (info != null && info.isSuccess()) {
                        mCallback.onSoldListLoaded(info);
                    } else mCallback.onError();
                } else mCallback.onError();
            }

            @Override
            public void onFailure(@NotNull Call<DomainGoodsInfo> call, Throwable t) {
                mCallback.onError();
            }
        });
    }

    @Override
    public void registerCallback(ISoldCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISoldCallback callback) {
        mCallback = null;
    }
}

package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISSBPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SSBImpl implements ISSBPresenter {

    private ISSBCallback mCallback;

    @Override
    public void getSoldList(String token, int page) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getMySoldGoods(token, page);
        task.enqueue(new CallBackWithRetry<DomainGoodsInfo>(task) {
            @Override
            public void onResponse(@NotNull Call<DomainGoodsInfo> call, @NotNull Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainGoodsInfo info = response.body();
                        if (info != null && info.isSuccess()) {
                            mCallback.onSoldListLoaded(info);
                        } else mCallback.onError();
                    } else mCallback.onError();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void getBoughtList(String token, int page) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getMyBoughtGoods(token, page);
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(@NotNull Call<DomainGoodsInfo> call, @NotNull Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainGoodsInfo info = response.body();
                        if (info != null && info.isSuccess()) {
                            mCallback.onBoughtListLoaded(info);
                        } else mCallback.onError();
                    } else mCallback.onError();
            }

            @Override
            public void onFailure(@NotNull Call<DomainGoodsInfo> call, Throwable t) {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void getSellingList(String token, int page) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getMySellingGoods(token, page);
        task.enqueue(new CallBackWithRetry<DomainGoodsInfo>(task) {
            @Override
            public void onResponse(@NotNull Call<DomainGoodsInfo> call, @NotNull Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainGoodsInfo info = response.body();
                        if (info != null && info.isSuccess()) {
                            mCallback.onSellingListLoaded(info);
                        } else mCallback.onError();
                    } else mCallback.onError();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void unListItem(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.unListItem(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainResult result = response.body();
                        if (result != null && result.isSuccess())
                            mCallback.onUnListItemSuccess();
                        else
                            mCallback.onActionFailed();
                    } else
                        mCallback.onActionFailed();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void registerCallback(ISSBCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISSBCallback callback) {
        mCallback = null;
    }
}

package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IGoodsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodsImpl implements IGoodsPresenter {
    private IGoodsCallback mCallback;

    @Override
    public void quoteItem(String token, String goodsID, String quotePrice) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.quoteRequest(token, goodsID, quotePrice);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    if (mCallback != null)
                        if (result != null && result.isSuccess())
                            mCallback.onQuoteSuccess();
                        else
                            mCallback.onError();
                } else {
                    if (mCallback != null)
                        mCallback.onError();

                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void favoriteItem(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.favorite(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (mCallback != null)
                        if (result != null && result.isSuccess())
                            mCallback.onFavoriteSuccess();
                        else
                            mCallback.onError();
                } else if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void isItemFavored(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.isItemFavored(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (mCallback != null) {
                        if (result != null) {
                            mCallback.onIsFavorGet(result.isSuccess());
                        } else mCallback.onError();
                    }
                } else if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void unFavorItem(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.unFavoriteItem(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (mCallback != null) {
                        if (result != null && result.isSuccess()) {
                            mCallback.onUnFavorSuccess();
                        } else mCallback.onError();
                    }
                } else if (mCallback != null)
                    mCallback.onError();
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

package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.api.Api;
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.api.base.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ILikesPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ILikesCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LikesImpl implements ILikesPresenter {

    private static LikesImpl mLikeImpl = null;

    public static LikesImpl getInstance() {
        if (mLikeImpl == null) {
            mLikeImpl = new LikesImpl();
        }
        return mLikeImpl;
    }

    private ILikesCallback mCallback;

    @Override
    public void getLikes(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getFavorites(token);
        task.enqueue(new CallBackWithRetry<DomainGoodsInfo>(task) {
            @Override
            public void onFailureAllRetries() {
                mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();
                    if (mCallback != null)
                        if (info != null && info.isSuccess()) {
                            mCallback.onLikesLoaded(info);
                        } else mCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerCallback(ILikesCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ILikesCallback callback) {
        mCallback = null;
    }
}

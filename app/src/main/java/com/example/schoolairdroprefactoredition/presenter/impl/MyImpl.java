package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.model.api.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IMyPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyImpl implements IMyPresenter {

    private IMyCallback mCallback = null;

    @Override
    public void getUserInfo(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainUserInfo> task = api.getUserInfo(token);
        task.enqueue(new CallBackWithRetry<DomainUserInfo>(task) {
            @Override
            public void onResponse(Call<DomainUserInfo> call, Response<DomainUserInfo> response) {
                int code = response.code();
                Response<DomainUserInfo> respond = response;
                DomainUserInfo info = respond.body();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        if (info != null && info.isSuccess()) {
                            mCallback.onUserInfoLoaded(info);
                        } else
                            mCallback.onError();
                    } else if (code == HttpURLConnection.HTTP_UNAUTHORIZED)
                        mCallback.onError();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void registerCallback(IMyCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IMyCallback callback) {
        mCallback = null;
    }
}

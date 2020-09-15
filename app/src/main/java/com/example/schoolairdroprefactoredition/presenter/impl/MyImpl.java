package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IMyPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyImpl implements IMyPresenter {

    private IMyCallback mCallback = null;

    @Override
    public void getUserInfo(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainUserInfo> task = api.getUserInfo(token);
        task.enqueue(new Callback<DomainUserInfo>() {
            @Override
            public void onResponse(Call<DomainUserInfo> call, Response<DomainUserInfo> response) {
                int code = response.code();
                Response<DomainUserInfo> respond = response;
                DomainUserInfo info = respond.body();
                if (code == HttpURLConnection.HTTP_OK) {

                    if (info != null && info.isSuccess()) {
                        mCallback.onUserInfoLoaded(info);
                    } else
                        mCallback.onError();
                } else
                    mCallback.onError();
            }

            @Override
            public void onFailure(Call<DomainUserInfo> call, Throwable t) {
                Log.e("MyImpl", "请求失败 -- > " + t);
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

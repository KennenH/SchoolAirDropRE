package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IUserNamePresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IResultCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserNameImpl implements IUserNamePresenter {

    private IResultCallback mCallback;

    @Override
    public void rename(String token, String name) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.updateUserName(token, name);
        task.enqueue(new Callback<DomainResult>() {
            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult body = response.body();
                    if (body != null && body.isSuccess())
                        mCallback.onSuccess();
                    else
                        mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainResult> call, Throwable t) {
                mCallback.onError();
                LogUtils.d("请求失败 -- > " + t.toString());
            }
        });

    }

    @Override
    public void registerCallback(IResultCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IResultCallback callback) {
        mCallback = null;
    }

}

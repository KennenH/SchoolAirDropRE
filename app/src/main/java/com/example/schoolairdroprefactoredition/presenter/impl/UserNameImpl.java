package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainModifyResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IUserNamePresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserNameCallback;

import retrofit2.Call;
import retrofit2.Retrofit;

public class UserNameImpl implements IUserNamePresenter {

    private IUserNameCallback mCallback;

    @Override
    public void rename(String token,String name) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainModifyResult> task = api.updateUserName(token,name);

    }

    @Override
    public void registerCallback(IUserNameCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IUserNameCallback callback) {
        mCallback = null;
    }

}

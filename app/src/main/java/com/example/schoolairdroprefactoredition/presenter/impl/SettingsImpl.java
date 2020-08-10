package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizePost;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISettingsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;
import com.example.schoolairdroprefactoredition.utils.RSACoder;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsImpl implements ISettingsPresenter {
    private ISettingsCallback mCallback;

    @Override
    public void getPublicKey() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainAuthorizeGet> task = api.getAuthorizePublicKey();
        task.enqueue(new Callback<DomainAuthorizeGet>() {
            @Override
            public void onResponse(Call<DomainAuthorizeGet> call, Response<DomainAuthorizeGet> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAuthorizeGet authorization = response.body();
                    if (mCallback != null) {
                        mCallback.onPublicKeyGot(authorization);
                    }
                } else {
                    Log.d("HomeImpl", "请求错误 " + code);
                }
            }

            @Override
            public void onFailure(Call<DomainAuthorizeGet> call, Throwable t) {
                Log.e("HomeImpl", "请求失败 -- > " + t);
            }
        });
    }

    @Override
    public void postAlipayIDRSA(String publicKey, String alipayID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        mCallback.onAuthorizationSuccess(RSACoder.encryptByPublicKey(publicKey, alipayID));
//        Call<DomainAuthorizePost> task = api.postAlipayIDRSA(RSACoder.encryptByPublicKey(publicKey, alipayID));
//        task.enqueue(new Callback<DomainAuthorizePost>() {
//            @Override
//            public void onResponse(Call<DomainAuthorizePost> call, Response<DomainAuthorizePost> response) {
//                int code = response.code();
//                if (code == HttpURLConnection.HTTP_OK) {
//                    DomainAuthorizePost authorization = response.body();
//                    if (mCallback != null) {
//                        mCallback.onAuthorizationSuccess(authorization);
//                    }
//                } else {
//                    Log.d("HomeImpl", "请求错误 " + code);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DomainAuthorizePost> call, Throwable t) {
//                Log.e("HomeImpl", "请求失败 -- > " + t);
//            }
//        });
    }

    @Override
    public void registerCallback(ISettingsCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISettingsCallback callback) {
        mCallback = null;
    }
}

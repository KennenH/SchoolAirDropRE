package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISettingsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;
import com.example.schoolairdroprefactoredition.utils.RSACoder;

import java.io.IOException;
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
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainAuthorizeGet> call, Throwable t) {
                Log.e("HomeImpl", "请求失败 -- > " + t);
                mCallback.onError();
            }
        });
    }

    /**
     * 获取的公钥将Alipay id 加密后携带一系列参数post回去请求token
     *
     * @param sessionID    Header
     * @param grantType    Field
     * @param clientID     Field
     * @param clientSecret Field
     * @param rawAlipay    未加密的Alipay id
     * @param publicKey    公钥
     */
    @Override
    public void postAlipayIDRSA(String sessionID, String grantType, String clientID, String clientSecret, String rawAlipay, String publicKey) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Log.d("Impl", "encrypted alipay == > " + RSACoder.encryptWithPublicKey(publicKey, rawAlipay) +
                "\nsession id == > " + sessionID);
        Call<DomainAuthorize> task = api.authorize(sessionID, grantType, clientID, clientSecret, RSACoder.encryptWithPublicKey(publicKey, rawAlipay));
        task.enqueue(new Callback<DomainAuthorize>() {
            @Override
            public void onResponse(Call<DomainAuthorize> call, Response<DomainAuthorize> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAuthorize authorization = response.body();
                    if (mCallback != null) {
                        mCallback.onAuthorizationSuccess(authorization);
                    }
                } else {
                    try {
                        Log.d("SettingsImpl", "请求错误 " + code + " response -- > " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainAuthorize> call, Throwable t) {
                Log.e("SettingsImpl", "请求失败 -- > " + t);
                mCallback.onError();
            }
        });
    }

    @Override
    public void getUserInfo(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGetUserInfo> task = api.getUserInfo(token);
        task.enqueue(new Callback<DomainGetUserInfo>() {
            @Override
            public void onResponse(Call<DomainGetUserInfo> call, Response<DomainGetUserInfo> response) {
                int code = response.code();
                Response<DomainGetUserInfo> respond = response;
                Log.d("SettingsImpl", respond.toString());
                DomainGetUserInfo info = respond.body();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (info != null && info.isSuccess()) {
                        mCallback.onUserInfoLoaded(info);
                    } else
                        mCallback.onError();
                } else
                    mCallback.onError();
            }

            @Override
            public void onFailure(Call<DomainGetUserInfo> call, Throwable t) {
                Log.e("MyImpl", "请求失败 -- > " + t);
                mCallback.onError();
            }
        });
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

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

import okhttp3.ResponseBody;
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
                    String cookie = response.headers().get("Set-Cookie");
                    String session = null;

                    if (cookie != null)
                        session = cookie.substring(0, cookie.indexOf(';'));

                    if (authorization != null)
                        authorization.setCookie(session);
//
//                    Log.d("getPublicKey", authorization.toString());

                    if (mCallback != null)
                        mCallback.onPublicKeyGot(authorization);
                } else {
//                    try {
//                        Log.d("SettingsImpl", "请求错误 " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainAuthorizeGet> call, Throwable t) {
                mCallback.onError();
            }
        });
    }

    /**
     * 获取的公钥将Alipay id 加密后携带一系列参数post回去请求token
     *
     * @param cookie       Header
     * @param grantType    Field
     * @param clientID     Field
     * @param clientSecret Field
     * @param rawAlipay    未加密的Alipay id
     * @param publicKey    公钥
     */
    @Override
    public void postAlipayIDRSA(String cookie, String grantType, String clientID, String clientSecret, String rawAlipay, String publicKey) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
//        Log.d("postAlipayIDRSA", "encrypted alipay -- > " + RSACoder.encryptWithPublicKey(publicKey, rawAlipay) +
//                "cookie -- > " + cookie);
        Call<DomainAuthorize> task = api.authorize(cookie, grantType, clientID, clientSecret, RSACoder.encryptWithPublicKey(publicKey, rawAlipay));
        task.enqueue(new Callback<DomainAuthorize>() {
            @Override
            public void onResponse(Call<DomainAuthorize> call, Response<DomainAuthorize> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAuthorize authorization = response.body();

//                    try {
//                        Log.d("postAlipayIDRSA", "response.body.string -- > " + authorization.string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    Log.d("postAlipayIDRSA", authorization.toString());

                    if (mCallback != null) {
                        mCallback.onAuthorizationSuccess(authorization);
                    }
                } else {
                    try {
                        Log.d("postAlipayIDRSA", "请求错误 " + code + " response -- > " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainAuthorize> call, Throwable t) {
                Log.e("postAlipayIDRSA", "请求失败 -- > " + t);
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
                DomainGetUserInfo info = response.body();
                if (code == HttpURLConnection.HTTP_OK) {

//                    try {
//                        Log.d("getUserInfo", info.string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    if (info != null && info.isSuccess()) {
                        mCallback.onUserInfoLoaded(info);
                    } else
                        mCallback.onError();
                } else {
                    try {
                        Log.d("getUserInfo", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainGetUserInfo> call, Throwable t) {
                Log.e("getUserInfo", "请求失败 -- > " + t);
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

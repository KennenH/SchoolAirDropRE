package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.cache.UserInfoCache;
import com.example.schoolairdroprefactoredition.cache.UserTokenCache;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ILoginPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ILoginCallback;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.RSACoder;
import com.example.schoolairdroprefactoredition.utils.UserLoginCacheUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginImpl implements ILoginPresenter {
    private ILoginCallback mCallback;

    /**
     * 退出登录
     * 清除本地用户信息和token
     */
    @Override
    public void logout() {
        UserLoginCacheUtils.deleteCache(UserTokenCache.USER_TOKEN);
        UserLoginCacheUtils.deleteCache(UserInfoCache.USER_INFO);
    }

    @Override
    public void getPublicKey() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainAuthorizeGet> task = api.getAuthorizePublicKey();
        task.enqueue(new CallBackWithRetry<DomainAuthorizeGet>(task) {
            @Override
            public void onFailureAllRetries() {
                mCallback.onLoginError();
            }

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
                    else {
                        if (mCallback != null)
                            mCallback.onLoginError();
                    }
//
                    if (mCallback != null)
                        mCallback.onPublicKeyGot(authorization);
                } else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    if (mCallback != null)
                        mCallback.onTokenInvalid();
                } else {
                    if (mCallback != null)
                        mCallback.onLoginError();
                }
//                    try {
//                        Log.d("SettingsImpl", "请求错误 " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

            }
        });
    }

    /**
     * 获取的公钥将Alipay id 加密后携带一系列参数post回去请求token
     *
     * @param cookie    Header
     * @param rawAlipay 未加密的Alipay id
     * @param publicKey 公钥
     */
    @Override
    public void postAlipayIDRSA(String cookie, String rawAlipay, String publicKey, String registrationID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);

        Call<DomainAuthorize> task = api.authorize(cookie,
                ConstantUtil.CLIENT_GRANT_TYPE,
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET,
                RSACoder.encryptWithPublicKey(publicKey, rawAlipay),
                registrationID);
        task.enqueue(new CallBackWithRetry<DomainAuthorize>(task) {
            @Override
            public void onResponse(Call<DomainAuthorize> call, Response<DomainAuthorize> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAuthorize authorization = response.body();

                    // token 有效期为一小时
                    if (authorization != null)
                        UserLoginCacheUtils.saveUserToken(authorization, 3600_000);
                    else {
                        if (mCallback != null)
                            mCallback.onLoginError();
                    }
//                    try {
//                        Log.d("postAlipayIDRSA", "response.body.string -- > " + authorization.string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    if (mCallback != null) {
                        mCallback.onAuthorizationSuccess(authorization);
                    }
                } else {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mCallback != null)
                        mCallback.onLoginError();
                }
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onLoginError();
            }
        });
    }

    @Override
    public void getUserInfo(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainUserInfo> task = api.getUserInfo(token);
        task.enqueue(new CallBackWithRetry<DomainUserInfo>(task) {
            @Override
            public void onResponse(Call<DomainUserInfo> call, Response<DomainUserInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainUserInfo info = response.body();

//                    try {
//                        Log.d("getUserInfo", info.string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    if (mCallback != null)
                        if (info != null && info.isSuccess()) {
                            mCallback.onUserInfoLoaded(info);
                            UserLoginCacheUtils.saveUserInfo(info.getData().get(0));
                        } else
                            mCallback.onLoginError();
                } else {
                    try {
                        Log.d("getUserInfo", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mCallback != null)
                        mCallback.onLoginError();
                }
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onLoginError();
            }
        });
    }

    @Override
    public void registerCallback(ILoginCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ILoginCallback callback) {
        mCallback = null;
    }
}

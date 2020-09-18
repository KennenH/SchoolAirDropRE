package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.cache.UserCache;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorizeGet;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ILoginPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ILoginCallback;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;
import com.example.schoolairdroprefactoredition.utils.RSACoder;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginImpl implements ILoginPresenter {
    private ILoginCallback mCallback;

    private JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

    /**
     * 保存用户上一次登录获取的用户token
     * 以便下次用户在token有效期内登录时自动登录
     *
     * @param token 本次登录获取到的token
     */
    private void saveUserToken(DomainAuthorize token) {
        UserCache userCache = mJsonCacheUtil.getValue(UserCache.USER_CACHE, UserCache.class);
        if (userCache == null) userCache = new UserCache();
        userCache.setToken(token);
        mJsonCacheUtil.saveCache(UserCache.USER_CACHE, userCache);
    }

    /**
     * 保存上次登录后获取的用户信息
     * 以便下次打开app时即时加载
     *
     * @param info 本次登录获取的用户信息
     */
    private void saveUserInfo(DomainUserInfo.DataBean info) {
        UserCache userCache = mJsonCacheUtil.getValue(UserCache.USER_CACHE, UserCache.class);
        if (userCache == null) userCache = new UserCache();
        userCache.setInfo(info);
        mJsonCacheUtil.saveCache(UserCache.USER_CACHE, userCache);
    }

    /**
     * 退出登录
     * 清除本地用户信息和token
     */
    @Override
    public void logout() {
        mJsonCacheUtil.deleteCache(UserCache.USER_CACHE);
    }

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
        Call<DomainAuthorize> task = api.authorize(cookie, grantType, clientID, clientSecret, RSACoder.encryptWithPublicKey(publicKey, rawAlipay));
        task.enqueue(new Callback<DomainAuthorize>() {
            @Override
            public void onResponse(Call<DomainAuthorize> call, Response<DomainAuthorize> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAuthorize authorization = response.body();
                    saveUserToken(authorization);

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
        Call<DomainUserInfo> task = api.getUserInfo(token);
        task.enqueue(new Callback<DomainUserInfo>() {
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
                    if (info != null && info.isSuccess()) {
                        mCallback.onUserInfoLoaded(info);
                        saveUserInfo(info.getData().get(0));
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
            public void onFailure(Call<DomainUserInfo> call, Throwable t) {
                Log.e("getUserInfo", "请求失败 -- > " + t);
                mCallback.onError();
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

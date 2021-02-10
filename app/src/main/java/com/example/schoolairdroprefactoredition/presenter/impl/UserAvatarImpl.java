package com.example.schoolairdroprefactoredition.presenter.impl;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.api.base.RetrofitManager;
import com.example.schoolairdroprefactoredition.api.Api;
import com.example.schoolairdroprefactoredition.presenter.IUserAvatarPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;
import com.example.schoolairdroprefactoredition.utils.FileUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserAvatarImpl implements IUserAvatarPresenter {

    private static UserAvatarImpl mUserAvatarImpl = null;

    public static UserAvatarImpl getInstance() {
        if (mUserAvatarImpl == null) {
            mUserAvatarImpl = new UserAvatarImpl();
        }
        return mUserAvatarImpl;
    }

    private IUserAvatarCallback mCallback;

    @Override
    public void updateAvatar(Context context, String token, String img) {
        try {
            Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
            Api api = retrofit.create(Api.class);

            MultipartBody.Part photo = FileUtil.createPartWithPath(context, "photo", img, false);

            retrofit2.Call<DomainAvatarUpdateResult> task = api.updateAvatar(photo);
            task.enqueue(new CallBackWithRetry<DomainAvatarUpdateResult>(task) {
                @Override
                public void onResponse(retrofit2.Call<DomainAvatarUpdateResult> call, Response<DomainAvatarUpdateResult> response) {
                    int code = response.code();
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainAvatarUpdateResult body = response.body();
                        if (body != null)
                            if (body.isSuccess()) {

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
                                if (mCallback != null)
                                    mCallback.onUpdateSuccess(body);
                            } else if (mCallback != null)
                                mCallback.onError();
                    } else {
                        try {
                            LogUtils.d(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mCallback != null)
                            mCallback.onError();
                    }
                }

                @Override
                public void onFailureAllRetries() {
                    if (mCallback != null)
                        mCallback.onError();
                }
            });
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.onError();
            }
        }
    }

    @Override
    public void registerCallback(IUserAvatarCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IUserAvatarCallback callback) {
        mCallback = null;
    }
}

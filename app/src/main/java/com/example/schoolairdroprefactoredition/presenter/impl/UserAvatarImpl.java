package com.example.schoolairdroprefactoredition.presenter.impl;

import android.os.Handler;
import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdate;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IUserAvatarPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;

import java.io.File;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserAvatarImpl implements IUserAvatarPresenter {
    private IUserAvatarCallback mCallback;

    @Override
    public void sendAvatar(String img, String uid) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        File file = new File(img);

        MultipartBody.Part photo = MultipartBody.Part.createFormData(
                "photo",
                file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), uid);

        retrofit2.Call<DomainAvatarUpdate> task = api.updateAvatar(photo, id);
        task.enqueue(new Callback<DomainAvatarUpdate>() {
            @Override
            public void onResponse(retrofit2.Call<DomainAvatarUpdate> call, Response<DomainAvatarUpdate> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainAvatarUpdate body = response.body();
                    if (body.isSuccess()) {
                        Log.d("UserAvatarImpl", "code -- > " + code + " message -- > " + response.toString());
                        mCallback.onSent(body);
                    } else mCallback.onError();
                } else {
                    Log.d("UserAvatarImpl", "code -- > " + code + " message -- > " + response.toString());
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainAvatarUpdate> call, Throwable t) {
                mCallback.onError();
            }
        });
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

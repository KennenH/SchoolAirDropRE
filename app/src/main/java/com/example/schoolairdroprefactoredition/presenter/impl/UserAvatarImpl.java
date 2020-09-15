package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IUserAvatarPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserAvatarImpl implements IUserAvatarPresenter {
    private IUserAvatarCallback mCallback;

    @Override
    public void updateAvatar(String token, String img) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        File file = new File(img);

        MultipartBody.Part photo = MultipartBody.Part.createFormData(
                "photo",
                file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));

        retrofit2.Call<ResponseBody> task = api.updateAvatar(token, photo);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null)
//                        if (body.isSuccess()) {

                    {
                        try {
                            LogUtils.d(body.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

//                            mCallback.onUpdateSuccess(body);
//                        } else mCallback.onError();
                } else {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.d(t.toString());
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

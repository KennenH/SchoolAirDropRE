package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IOnGoingPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnGoingImpl implements IOnGoingPresenter {

    private IOnGoingCallback mCallback;

    @Override
    public void getMyReceived(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainOnGoing> task = api.getEventMyReceived(token);
        task.enqueue(new CallBackWithRetry<DomainOnGoing>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainOnGoing> call, Response<DomainOnGoing> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainOnGoing receive = response.body();
                    if (mCallback != null)
                        if (receive != null && receive.getSuccess()) {
                            mCallback.onEventMyReceivedLoaded(receive.getData());
                        } else
                            mCallback.onError();
                } else if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void getMySent(String token) {
        if (mCallback != null)
            mCallback.onEventMySentLoaded(new ArrayList<>());
    }

    @Override
    public void registerCallback(IOnGoingCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IOnGoingCallback callback) {
        mCallback = null;
    }

}

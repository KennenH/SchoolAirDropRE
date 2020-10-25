package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainQuote;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IQuoteSectionsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuoteSectionImpl implements IQuoteSectionsPresenter {

    private IQuoteSectionsCallback mCallback;

    @Override
    public void getReceivedQuote(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainQuote> task = api.getQuoteMyReceived(token);
        task.enqueue(new CallBackWithRetry<DomainQuote>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainQuote> call, Response<DomainQuote> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    DomainQuote info = response.body();
                    if (mCallback != null)
                        if (info != null && info.isSuccess())
                            mCallback.onQuoteReceivedLoaded(info.getData());
                        else
                            mCallback.onError();
                } else if (mCallback != null) {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void getSentQuote(String token) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainQuote> task = api.getQuoteMySend(token);
        task.enqueue(new CallBackWithRetry<DomainQuote>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainQuote> call, Response<DomainQuote> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    DomainQuote info = response.body();
                    if (mCallback != null)
                        if (info != null && info.isSuccess())
                            mCallback.onQuoteSentLoaded(info.getData());
                        else
                            mCallback.onError();
                } else {
                    if (mCallback != null)
                        mCallback.onError();

                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void acceptQuote(String token, String quoteID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.acceptQuote(token, quoteID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    DomainResult result = response.body();
                    if (mCallback != null)
                        if (result != null && result.isSuccess())
                            mCallback.onAcceptQuoteSuccess();
                        else
                            mCallback.onError();

                } else if (mCallback != null) {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void refuseQuote(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.refuseQuote(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DomainResult result = response.body();
                    if (mCallback != null)
                        if (result != null && result.isSuccess())
                            mCallback.onRefuseQuoteSuccess();
                        else
                            mCallback.onError();

                } else if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void registerCallback(IQuoteSectionsCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IQuoteSectionsCallback callback) {
        mCallback = null;
    }
}

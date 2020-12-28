package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.api.base.RetrofitManager;
import com.example.schoolairdroprefactoredition.api.Api;
import com.example.schoolairdroprefactoredition.presenter.ISSBPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SSBImpl implements ISSBPresenter {

    private static SSBImpl mSSBImpl = null;

    public static SSBImpl getInstance() {
        if (mSSBImpl == null) {
            mSSBImpl = new SSBImpl();
        }
        return mSSBImpl;
    }

    private ISSBCallback mCallback;

    @Override
    public void getSellingList(String token, int page) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<HomeGoodsListInfo> task = api.getMySellingGoods(token, page);
        task.enqueue(new CallBackWithRetry<HomeGoodsListInfo>(task) {
            @Override
            public void onResponse(@NotNull Call<HomeGoodsListInfo> call, @NotNull Response<HomeGoodsListInfo> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        HomeGoodsListInfo info = response.body();
                        if (info != null && info.isSuccess()) {
                            mCallback.onSellingLoaded(info);
                        } else mCallback.onError();
                    } else mCallback.onError();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void unListItem(String token, String goodsID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainResult> task = api.unListItem(token, goodsID);
        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onResponse(Call<DomainResult> call, Response<DomainResult> response) {
                int code = response.code();
                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        DomainResult result = response.body();

//                        try {
//                            LogUtils.d(response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        if (result != null && result.isSuccess())
                            mCallback.onUnListItemSuccess();
                        else
                            mCallback.onActionFailed();
                    } else
                        mCallback.onActionFailed();
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void getSellingByUID(int userID) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<HomeGoodsListInfo> task = api.getUserSellingByID(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, userID);
        task.enqueue(new CallBackWithRetry<HomeGoodsListInfo>(task) {
            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
            }

            @Override
            public void onResponse(@NotNull Call<HomeGoodsListInfo> call, @NotNull Response<HomeGoodsListInfo> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    HomeGoodsListInfo info = response.body();

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    if (mCallback != null)
                        if (info != null && info.isSuccess()) {
                            mCallback.onSellingLoaded(info);
                        } else
                            mCallback.onError();
                } else if (mCallback != null)
                    mCallback.onError();
            }
        });
    }

    @Override
    public void getPosts(String token, int page) {

    }

    @Override
    public void deletePost(String token, String postID) {

    }

    @Override
    public void registerCallback(ISSBCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISSBCallback callback) {
        mCallback = null;
    }
}

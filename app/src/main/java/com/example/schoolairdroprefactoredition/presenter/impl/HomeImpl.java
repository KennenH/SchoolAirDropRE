package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainNews;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.presenter.IHomePresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeCallback;
import com.example.schoolairdroprefactoredition.utils.RetrofitManager;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    /**
     * 请求附近在售的数据
     */
    @Override
    public void getNearbyGoodsInfo() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getGoodsInfo();
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();
                    Log.d("HomeImpl", info.toString());
                    if (mCallback != null) {
                        mCallback.onGoodsInfoLoaded(info);
                    }
                } else {
                    Log.d("HomeImpl", "请求错误 " + code);
                }
            }

            @Override
            public void onFailure(Call<DomainGoodsInfo> call, Throwable t) {
                Log.e("HomeImpl", "请求失败 -- > " + t);
            }
        });
    }

    /**
     * 请求最新消息的数据
     */
    @Override
    public void getNews() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainNews> task = api.getNews();
        task.enqueue(new Callback<DomainNews>() {
            @Override
            public void onResponse(Call<DomainNews> call, Response<DomainNews> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainNews info = response.body();
                    Log.d("HomeImpl", info.toString());
                    if (mCallback != null) {
                        mCallback.onNewsLoaded(info);
                    }
                } else {
                    Log.d("HomeImpl", "请求错误 " + code);
                }
            }

            @Override
            public void onFailure(Call<DomainNews> call, Throwable t) {
                Log.e("HomeImpl", "请求失败 -- > " + t);
            }
        });
    }


    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        mCallback = null;
    }
}

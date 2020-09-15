package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IHomeGoodsInfoPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeGoodsInfoImpl implements IHomeGoodsInfoPresenter {

    private IHomeGoodsInfoCallback mCallback = null;

    /**
     * 请求附近在售的数据
     */
    @Override
    public void getNearbyGoods(int page, double longitude, double latitude) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getGoodsInfo( longitude, latitude);
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                DomainGoodsInfo info = response.body();

//                try {
//                    Log.d("HomeGoodsInfoImpl", info.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                Log.d("getNearbyGoods", "longitude -- > " + longitude + " latitude -- > " + latitude);
                if (code == HttpURLConnection.HTTP_OK) {
                    if (info != null && info.isSuccess()) {
                        mCallback.onNearbyGoodsLoaded(info.getData());
                    } else
                        mCallback.onError();
                } else
                    mCallback.onError();
            }

            @Override
            public void onFailure(Call<DomainGoodsInfo> call, Throwable t) {
                Log.e("HomeImpl", "请求失败 -- > " + t);
                mCallback.onError();
            }
        });
    }

    @Override
    public void registerCallback(IHomeGoodsInfoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeGoodsInfoCallback callback) {
        mCallback = null;
    }
}

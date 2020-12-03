package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.api.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.IHomeGoodsInfoPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeGoodsInfoImpl implements IHomeGoodsInfoPresenter {

    private static HomeGoodsInfoImpl mHomeGoodsInfoImpl = null;

    public static HomeGoodsInfoImpl getInstance() {
        if (mHomeGoodsInfoImpl == null) {
            mHomeGoodsInfoImpl = new HomeGoodsInfoImpl();
        }
        return mHomeGoodsInfoImpl;
    }

    private IHomeGoodsInfoCallback mCallback = null;

    /**
     * 请求附近在售的数据
     */
    @Override
    public void getNearbyGoods(int page, double longitude, double latitude) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getGoodsInfo(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, page, 120.36055, 30.31747);
        task.enqueue(new CallBackWithRetry<DomainGoodsInfo>(task) {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                DomainGoodsInfo info = response.body();

//                try {
//                    LogUtils.d(info.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                if (mCallback != null)
                    if (code == HttpURLConnection.HTTP_OK) {
                        if (info != null && info.isSuccess()) {
                            mCallback.onNearbyGoodsLoaded(info.getData());
                        } else
                            mCallback.onError();
                    } else {
                        mCallback.onError();
                    }
            }

            @Override
            public void onFailureAllRetries() {
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

package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeGoodsInfoImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class HomeNearbyFragmentViewModel extends BaseStateViewModel implements IHomeGoodsInfoCallback {
    private int nowPage;
    private double longitude;
    private double latitude;

    private HomeGoodsInfoImpl mHomeImpl;

    private MutableLiveData<List<DomainGoodsInfo.DataBean>> mGoodsInfo = new MutableLiveData<>();

    public HomeNearbyFragmentViewModel() {
        mHomeImpl = new HomeGoodsInfoImpl();
        mHomeImpl.registerCallback(this);
    }

    @Override
    public void onNearbyGoodsLoaded(List<DomainGoodsInfo.DataBean> goodsData) {
        mGoodsInfo.postValue(goodsData);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

    public LiveData<List<DomainGoodsInfo.DataBean>> getGoodsInfo(double longitude, double latitude) {
        nowPage = 0;
        this.longitude = longitude;
        this.latitude = latitude;

        mHomeImpl.getNearbyGoods(nowPage++, longitude, latitude);
        return mGoodsInfo;
    }

    public LiveData<List<DomainGoodsInfo.DataBean>> getGoodsInfo() {
        mHomeImpl.getNearbyGoods(nowPage, longitude, latitude);
        return mGoodsInfo;
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }

}

package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeGoodsInfoImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;

import java.util.List;

public class HomeNearbyFragmentViewModel extends BaseChildFragmentViewModel implements IHomeGoodsInfoCallback {

    private HomeGoodsInfoImpl mHomeImpl;

    private MutableLiveData<List<DomainGoodsInfo.GoodsInfoBean>> mGoodsInfo = new MutableLiveData<>();

    public HomeNearbyFragmentViewModel() {
        mHomeImpl = new HomeGoodsInfoImpl();
        mHomeImpl.registerCallback(this);
    }

    @Override
    public void onNearbyGoodsLoaded(List<DomainGoodsInfo.GoodsInfoBean> goodsData) {
        mGoodsInfo.postValue(goodsData);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

    @Override
    public void onLoading() {
        mOnRequestListener.onLoading();
    }

    public LiveData<List<DomainGoodsInfo.GoodsInfoBean>> getGoodsInfo(double longitude, double latitude) {
        mHomeImpl.getNearbyGoods(20, longitude, latitude);
        return mGoodsInfo;
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }

}

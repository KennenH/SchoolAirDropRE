package com.example.schoolairdroprefactoredition.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeGoodsInfoImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeNearbyFragmentViewModel extends ViewModel implements IHomeGoodsInfoCallback {

    private HomeGoodsInfoImpl mHomeImpl;

    private MutableLiveData<List<DomainGoodsInfo.GoodsInfoBean>> mGoodsInfo = new MutableLiveData<>();

    public HomeNearbyFragmentViewModel() {
        mHomeImpl = new HomeGoodsInfoImpl();
        mHomeImpl.registerCallback(this);
    }

    public LiveData<List<DomainGoodsInfo.GoodsInfoBean>> getGoodsInfo(double longitude, double latitude) {
        mHomeImpl.getNearbyGoods(20, longitude, latitude);
        return mGoodsInfo;
    }

    @Override
    public void onNearbyGoodsLoaded(List<DomainGoodsInfo.GoodsInfoBean> domainGoodsInfo) {
        mGoodsInfo.postValue(domainGoodsInfo);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onDataEmpty() {

    }

    @Override
    public void onPositionError() {

    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }

}

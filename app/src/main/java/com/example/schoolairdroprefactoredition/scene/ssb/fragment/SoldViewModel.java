package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SoldImpl;

public class SoldViewModel extends BaseStateViewModel implements ISoldCallback {

    private SoldImpl soldImpl;

    private MutableLiveData<DomainGoodsInfo> mSellingBeans = new MutableLiveData<>();

    public SoldViewModel() {
        soldImpl = new SoldImpl();
        soldImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getSoldBeans(String token) {
        soldImpl.getSoldList(token);
        return mSellingBeans;
    }

    @Override
    public void onSoldListLoaded(DomainGoodsInfo beans) {
        mSellingBeans.postValue(beans);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

}

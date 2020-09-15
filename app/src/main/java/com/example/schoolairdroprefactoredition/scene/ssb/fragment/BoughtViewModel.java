package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.BoughtImpl;

public class BoughtViewModel extends BaseStateViewModel implements IBoughtCallback {

    private BoughtImpl boughtImpl;

    private MutableLiveData<DomainGoodsInfo> mBoughtBeans = new MutableLiveData<>();

    public BoughtViewModel() {
        boughtImpl = new BoughtImpl();
        boughtImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getBoughtBeans(String token) {
        boughtImpl.getBoughtList(token);
        return mBoughtBeans;
    }

    @Override
    public void onBoughtListLoaded(DomainGoodsInfo beans) {
        mBoughtBeans.postValue(beans);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

    @Override
    public void onLoading() {
        mOnRequestListener.onLoading();
    }
}

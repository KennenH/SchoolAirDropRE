package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SSBImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class SSBViewModel extends BaseStateViewModel implements ISSBCallback {

    private SSBImpl ssbImpl;

    private MutableLiveData<DomainGoodsInfo> mSellingBeans = new MutableLiveData<>();
    private MutableLiveData<DomainGoodsInfo> mBoughtBeans = new MutableLiveData<>();
    private MutableLiveData<DomainGoodsInfo> mSoldBeans = new MutableLiveData<>();

    public SSBViewModel() {
        ssbImpl = new SSBImpl();
        ssbImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getSelling(String token) {
        ssbImpl.getSellingList(token);
        return mSellingBeans;
    }

    public LiveData<DomainGoodsInfo> getBought(String token) {
        ssbImpl.getBoughtList(token);
        return mBoughtBeans;
    }

    public LiveData<DomainGoodsInfo> getSold(String token) {
        ssbImpl.getSoldList(token);
        return mSoldBeans;
    }

    @Override
    public void onSellingListLoaded(DomainGoodsInfo selling) {
        mSellingBeans.postValue(selling);
    }

    @Override
    public void onSoldListLoaded(DomainGoodsInfo sold) {
        mSoldBeans.postValue(sold);
    }

    @Override
    public void onBoughtListLoaded(DomainGoodsInfo bought) {
        mBoughtBeans.postValue(bought);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}

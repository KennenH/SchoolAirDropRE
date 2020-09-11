package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SoldImpl;

import java.util.List;

public class SoldViewModel extends BaseChildFragmentViewModel implements ISoldCallback {

    private SoldImpl soldImpl;

    private MutableLiveData<DomainGoodsInfo> mSellingBeans;

    public SoldViewModel() {
        soldImpl = new SoldImpl();
        soldImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getSoldBeans() {
        soldImpl.getSoldList();
        return mSellingBeans;
    }

    @Override
    public void onSoldListLoaded(DomainGoodsInfo beans) {
        mSellingBeans = new MutableLiveData<>();
        mSellingBeans.postValue(beans);
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

package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.BoughtImpl;

import java.util.List;

public class BoughtViewModel extends BaseChildFragmentViewModel implements IBoughtCallback {

    private BoughtImpl boughtImpl;

    private MutableLiveData<DomainGoodsInfo> mBoughtBeans = new MutableLiveData<>();

    public BoughtViewModel() {
        boughtImpl = new BoughtImpl();
        boughtImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getBoughtBeans() {
        boughtImpl.getBoughtList();
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

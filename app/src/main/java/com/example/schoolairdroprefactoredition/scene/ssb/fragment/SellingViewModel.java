package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingImpl;

public class SellingViewModel extends BaseStateViewModel implements ISellingCallback {

    private SellingImpl sellingImpl;

    private MutableLiveData<DomainGoodsInfo> mSellingBeans = new MutableLiveData<>();

    public SellingViewModel() {
        sellingImpl = new SellingImpl();
        sellingImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getSelling(String token) {
        sellingImpl.getSelling(token);
        return mSellingBeans;
    }

    @Override
    public void onSellingLoaded(DomainGoodsInfo beans) {
        mSellingBeans.postValue(beans);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

}

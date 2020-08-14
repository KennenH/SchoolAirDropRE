package com.example.schoolairdroprefactoredition.fragment.ssb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.fragment.home.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SoldImpl;

import java.util.List;

public class SoldViewModel extends BaseChildFragmentViewModel implements ISoldCallback {

    private SoldImpl soldImpl;

    private MutableLiveData<List<TestSSBItemBean>> mSellingBeans;

    public SoldViewModel() {
        soldImpl = new SoldImpl();
        soldImpl.registerCallback(this);
    }

    public LiveData<List<TestSSBItemBean>> getSoldBeans() {
        soldImpl.getSoldList();
        return mSellingBeans;
    }

    @Override
    public void onSoldListLoaded(List<TestSSBItemBean> beans) {
        mSellingBeans = new MutableLiveData<>();
        mSellingBeans.setValue(beans);
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

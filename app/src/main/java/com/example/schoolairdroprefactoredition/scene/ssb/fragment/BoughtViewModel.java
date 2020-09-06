package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.BoughtImpl;

import java.util.List;

public class BoughtViewModel extends BaseChildFragmentViewModel implements IBoughtCallback {

    private BoughtImpl boughtImpl;

    private MutableLiveData<List<TestSSBItemBean>> mBoughtBeans;

    public BoughtViewModel() {
        boughtImpl = new BoughtImpl();
        boughtImpl.registerCallback(this);
    }

    public LiveData<List<TestSSBItemBean>> getBoughtBeans() {
        boughtImpl.getBoughtList();
        return mBoughtBeans;
    }

    @Override
    public void onBoughtListLoaded(List<TestSSBItemBean> beans) {
        mBoughtBeans = new MutableLiveData<>();
        mBoughtBeans.setValue(beans);
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

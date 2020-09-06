package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingImpl;

import java.util.List;

public class SellingViewModel extends BaseChildFragmentViewModel implements ISellingCallback {

    private SellingImpl sellingImpl;

    private MutableLiveData<List<TestSSBItemBean>> mSellingBeans;

    public SellingViewModel() {
        sellingImpl = new SellingImpl();
        sellingImpl.registerCallback(this);
    }

    public LiveData<List<TestSSBItemBean>> getSellingBeans() {
        sellingImpl.getSellingList();
        return mSellingBeans;
    }

    @Override
    public void onSellingListLoaded(List<TestSSBItemBean> beans) {
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

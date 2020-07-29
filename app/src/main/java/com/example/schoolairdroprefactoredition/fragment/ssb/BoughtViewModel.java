package com.example.schoolairdroprefactoredition.fragment.ssb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.BoughtImpl;

import java.util.List;

public class BoughtViewModel extends ViewModel implements IBoughtCallback {

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
}

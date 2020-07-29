package com.example.schoolairdroprefactoredition.fragment.ssb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingImpl;

import java.util.List;

public class SellingViewModel extends ViewModel implements ISellingCallback {

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
}

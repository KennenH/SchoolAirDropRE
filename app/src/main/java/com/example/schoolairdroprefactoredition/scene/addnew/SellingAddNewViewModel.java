package com.example.schoolairdroprefactoredition.scene.addnew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingAddNewImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class SellingAddNewViewModel extends BaseStateViewModel implements ISellingAddNewCallback {

    private MutableLiveData<DomainResult> mSubmitResult = new MutableLiveData<>();

    private SellingAddNewImpl sellingAddNewImpl;

    public SellingAddNewViewModel() {
        sellingAddNewImpl = new SellingAddNewImpl();
        sellingAddNewImpl.registerCallback(this);
    }

    public LiveData<DomainResult> submit(String token, String cover, List<String> picSet,
                                         String name, String description,
                                         double longitude, double latitude,
                                         boolean isBrandNew, boolean isQuotable, float price) {
        sellingAddNewImpl.submit(token, cover, picSet,
                name, description,
                longitude, latitude,
                isBrandNew, isQuotable, price);
        return mSubmitResult;
    }

    @Override
    public void onResult(DomainResult result) {
        mSubmitResult.postValue(result);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}

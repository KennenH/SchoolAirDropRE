package com.example.schoolairdroprefactoredition.activity.arrangeplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;
import com.example.schoolairdroprefactoredition.presenter.callback.IUnselectedTransactionCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UnselectedTransactionImpl;

import java.util.List;

public class UnselectedTransactionViewModel extends ViewModel implements IUnselectedTransactionCallback {

    private UnselectedTransactionImpl unselectedTransactionImpl;

    private MutableLiveData<List<DomainUnselectedTransaction>> mUnselectedTransactionList = new MutableLiveData<>();

    public LiveData<List<DomainUnselectedTransaction>> getUnselectedTransactionList() {
        unselectedTransactionImpl.getUnselectedTransaction();
        return mUnselectedTransactionList;
    }

    public UnselectedTransactionViewModel() {
        unselectedTransactionImpl = new UnselectedTransactionImpl();
        unselectedTransactionImpl.registerCallback(this);
    }

    @Override
    public void onTransactionLoaded(List<DomainUnselectedTransaction> list) {
        mUnselectedTransactionList.setValue(list);
    }

    @Override
    public void onNoTransaction() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }
}

package com.example.schoolairdroprefactoredition.scene.arrangeplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;
import com.example.schoolairdroprefactoredition.presenter.callback.IUnselectedTransactionCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UnselectedTransactionImpl;

import java.util.List;

public class UnArrangePositionTransactionViewModel extends ViewModel implements IUnselectedTransactionCallback {

    private final UnselectedTransactionImpl unselectedTransactionImpl;

    private final MutableLiveData<List<DomainUnselectedTransaction>> mUnselectedTransactionList = new MutableLiveData<>();

    public LiveData<List<DomainUnselectedTransaction>> getUnselectedTransactionList() {
        unselectedTransactionImpl.getUnselectedTransaction();
        return mUnselectedTransactionList;
    }

    public UnArrangePositionTransactionViewModel() {
        unselectedTransactionImpl = UnselectedTransactionImpl.getInstance();
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
}

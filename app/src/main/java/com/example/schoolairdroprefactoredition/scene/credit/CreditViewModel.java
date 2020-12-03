package com.example.schoolairdroprefactoredition.scene.credit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestCreditBean;
import com.example.schoolairdroprefactoredition.model.databean.TestCreditHistory;
import com.example.schoolairdroprefactoredition.presenter.callback.ICreditCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.CreditImpl;

import java.util.List;

public class CreditViewModel extends ViewModel implements ICreditCallback {
    private CreditImpl creditImpl;

    private MutableLiveData<TestCreditBean> mCredit;
    private MutableLiveData<List<TestCreditHistory>> mHistoryList;

    public LiveData<TestCreditBean> getCredit() {
        creditImpl.getCredit();
        return mCredit;
    }

    public LiveData<List<TestCreditHistory>> getCreditHistory() {
        creditImpl.getCreditHistory();
        return mHistoryList;
    }

    public CreditViewModel() {
        creditImpl = CreditImpl.getInstance();
        creditImpl.registerCallback(this);
    }

    @Override
    public void onCreditLoaded(TestCreditBean bean) {
        mCredit = new MutableLiveData<>();
        mCredit.postValue(bean);
    }

    @Override
    public void onCreditHistoryLoaded(List<TestCreditHistory> list) {
        mHistoryList = new MutableLiveData<>();
        mHistoryList.postValue(list);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        creditImpl.unregisterCallback(this);
    }
}

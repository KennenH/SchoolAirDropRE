package com.example.schoolairdroprefactoredition.activity.quote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteDetailBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteDetailCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteDetailImpl;

public class QuoteDetailViewModel extends ViewModel implements IQuoteDetailCallback {
    private QuoteDetailImpl quoteImpl;

    private MutableLiveData<TestQuoteDetailBean> mQuoteSection;

    public LiveData<TestQuoteDetailBean> getQuoteDetail(/* 报价单号 */) {
        quoteImpl.getQuoteDetail();
        return mQuoteSection;
    }

    public QuoteDetailViewModel() {
        quoteImpl = new QuoteDetailImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteDetailLoaded(TestQuoteDetailBean dataBeans) {
        mQuoteSection = new MutableLiveData<>();
        mQuoteSection.setValue(dataBeans);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        quoteImpl.unregisterCallback(this);
    }
}

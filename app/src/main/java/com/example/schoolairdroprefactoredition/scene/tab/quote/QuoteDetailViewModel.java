package com.example.schoolairdroprefactoredition.scene.tab.quote;

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
        quoteImpl = QuoteDetailImpl.getInstance();
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

package com.example.schoolairdroprefactoredition.fragment.quote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteSectionImpl;

import java.util.List;

public class QuoteViewModel extends ViewModel implements IQuoteSectionsCallback {
    private QuoteSectionImpl quoteImpl;

    private MutableLiveData<List<TestQuoteSectionItemBean>> mQuoteReceived;
    private MutableLiveData<List<TestQuoteSectionItemBean>> mQuoteSent;

    public LiveData<List<TestQuoteSectionItemBean>> getQuoteReceived() {
        quoteImpl.getReceivedQuote();
        return mQuoteReceived;
    }

    public LiveData<List<TestQuoteSectionItemBean>> getQuoteSent() {
        quoteImpl.getSentQuote();
        return mQuoteSent;
    }

    public QuoteViewModel() {
        quoteImpl = new QuoteSectionImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteReceivedLoaded(List<TestQuoteSectionItemBean> received) {
        mQuoteReceived = new MutableLiveData<>();
        mQuoteReceived.setValue(received);
    }

    @Override
    public void onQuoteSentLoaded(List<TestQuoteSectionItemBean> sent) {
        mQuoteSent = new MutableLiveData<>();
        mQuoteSent.setValue(sent);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        quoteImpl.unregisterCallback(this);
    }
}

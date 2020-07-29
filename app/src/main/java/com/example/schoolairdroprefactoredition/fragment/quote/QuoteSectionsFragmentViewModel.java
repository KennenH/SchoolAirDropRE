package com.example.schoolairdroprefactoredition.fragment.quote;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteSectionImpl;

import java.util.List;

public class QuoteSectionsFragmentViewModel extends ViewModel implements IQuoteSectionsCallback {
    private QuoteSectionImpl quoteImpl;

    private MutableLiveData<List<TestQuoteSectionItemBean>> mQuoteReceivedSection;
    private MutableLiveData<List<TestQuoteSectionItemBean>> mQuoteSentSection;

    public LiveData<List<TestQuoteSectionItemBean>> getQuoteReceivedSection() {
        quoteImpl.getReceivedQuoteSection();
        return mQuoteReceivedSection;
    }

    public LiveData<List<TestQuoteSectionItemBean>> getQuoteSentSection() {
        quoteImpl.getSentQuoteSection();
        return mQuoteSentSection;
    }

    public QuoteSectionsFragmentViewModel() {
        quoteImpl = new QuoteSectionImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteReceivedSectionLoaded(List<TestQuoteSectionItemBean> received) {
        mQuoteReceivedSection = new MutableLiveData<>();
        mQuoteReceivedSection.setValue(received);
    }

    @Override
    public void onQuoteSentSectionLoaded(List<TestQuoteSectionItemBean> sent) {
        mQuoteSentSection = new MutableLiveData<>();
        mQuoteSentSection.setValue(sent);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        quoteImpl.unregisterCallback(this);
    }
}

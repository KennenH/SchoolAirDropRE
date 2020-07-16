package com.example.schoolairdroprefactoredition.fragment.quote;

import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteReceiveItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteSectionImpl;

import java.util.List;

public class QuoteSectionsFragmentViewModel extends ViewModel implements IQuoteSectionsCallback {

    private QuoteSectionImpl quoteImpl;

    public QuoteSectionsFragmentViewModel() {
        quoteImpl = new QuoteSectionImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteLoaded(List<TestQuoteReceiveItemBean> dataBeans) {

    }

    @Override
    public void onQuoteEmpty() {

    }
}

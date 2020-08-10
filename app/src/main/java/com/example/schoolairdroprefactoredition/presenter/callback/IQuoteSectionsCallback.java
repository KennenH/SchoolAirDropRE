package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;

import java.util.List;

public interface IQuoteSectionsCallback {
    void onQuoteReceivedLoaded(List<TestQuoteSectionItemBean> received);

    void onQuoteSentLoaded(List<TestQuoteSectionItemBean> sent);
}

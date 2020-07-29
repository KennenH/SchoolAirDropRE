package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;

import java.util.List;

public interface IQuoteSectionsCallback {
    void onQuoteReceivedSectionLoaded(List<TestQuoteSectionItemBean> received);

    void onQuoteSentSectionLoaded(List<TestQuoteSectionItemBean> sent);
}

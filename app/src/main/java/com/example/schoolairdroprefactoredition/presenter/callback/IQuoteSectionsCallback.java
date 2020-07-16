package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteReceiveItemBean;

import java.util.List;

public interface IQuoteSectionsCallback {
    void onQuoteLoaded(List<TestQuoteReceiveItemBean> dataBeans);

    void onQuoteEmpty();
}

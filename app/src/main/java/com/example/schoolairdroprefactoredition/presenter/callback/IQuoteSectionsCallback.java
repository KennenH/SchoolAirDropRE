package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainQuote;

import java.util.List;

public interface IQuoteSectionsCallback extends IBaseCallback {
    void onQuoteReceivedLoaded(List<DomainQuote.DataBean> received);

    void onQuoteSentLoaded(List<DomainQuote.DataBean> sent);

    void onAcceptQuoteSuccess();

    void onRefuseQuoteSuccess();
}

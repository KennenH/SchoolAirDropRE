package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

public interface IQuoteSectionsPresenter extends IBasePresenter<IQuoteSectionsCallback> {
    void getReceivedQuote(String token);

    void getSentQuote(String token);

    void acceptQuote(String token, String quoteID);

    void refuseQuote(String token, String quoteID);
}

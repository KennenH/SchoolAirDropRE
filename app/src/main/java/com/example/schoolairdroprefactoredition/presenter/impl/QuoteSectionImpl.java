package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.IQuoteSectionsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

import java.util.ArrayList;

public class QuoteSectionImpl implements IQuoteSectionsPresenter {

    private IQuoteSectionsCallback mCallback;

    @Override
    public void getReceivedQuote(String token) {
        mCallback.onQuoteReceivedLoaded(new ArrayList<>());
    }

    @Override
    public void getSentQuote(String token) {
        mCallback.onQuoteSentLoaded(new ArrayList<>());
    }

    @Override
    public void registerCallback(IQuoteSectionsCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IQuoteSectionsCallback callback) {
        mCallback = null;
    }
}

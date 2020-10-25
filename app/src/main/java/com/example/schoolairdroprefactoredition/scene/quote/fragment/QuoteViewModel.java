package com.example.schoolairdroprefactoredition.scene.quote.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainQuote;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteSectionImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class QuoteViewModel extends BaseStateViewModel implements IQuoteSectionsCallback {
    private QuoteSectionImpl quoteImpl;

    private MutableLiveData<List<DomainQuote.DataBean>> mQuoteReceived = new MutableLiveData<>();
    private MutableLiveData<List<DomainQuote.DataBean>> mQuoteSent = new MutableLiveData<>();
    private MutableLiveData<Boolean> mAcceptResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRefuseResult = new MutableLiveData<>();

    public LiveData<List<DomainQuote.DataBean>> getQuoteReceived(String token) {
        quoteImpl.getReceivedQuote(token);
        return mQuoteReceived;
    }

    public LiveData<List<DomainQuote.DataBean>> getQuoteSent(String token) {
        quoteImpl.getSentQuote(token);
        return mQuoteSent;
    }

    public LiveData<Boolean> acceptQuote(String token, String goodsID) {
        quoteImpl.acceptQuote(token, goodsID);
        return mAcceptResult;
    }

    public LiveData<Boolean> refuseQuote(String token, String goodsID) {
        quoteImpl.refuseQuote(token, goodsID);
        return mRefuseResult;
    }

    public QuoteViewModel() {
        quoteImpl = new QuoteSectionImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteReceivedLoaded(List<DomainQuote.DataBean> received) {
        mQuoteReceived.postValue(received);
    }

    @Override
    public void onQuoteSentLoaded(List<DomainQuote.DataBean> sent) {
        mQuoteSent.postValue(sent);
    }

    @Override
    public void onAcceptQuoteSuccess() {
        mAcceptResult.postValue(true);
    }

    @Override
    public void onRefuseQuoteSuccess() {
        mRefuseResult.postValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        quoteImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onError();
        }
    }
}

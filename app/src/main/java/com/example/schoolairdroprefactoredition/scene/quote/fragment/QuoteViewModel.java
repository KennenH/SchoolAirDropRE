package com.example.schoolairdroprefactoredition.scene.quote.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.QuoteSectionImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class QuoteViewModel extends BaseStateViewModel implements IQuoteSectionsCallback {
    private QuoteSectionImpl quoteImpl;

    private MutableLiveData<List<DomainGoodsInfo.DataBean>> mQuoteReceived;
    private MutableLiveData<List<DomainGoodsInfo.DataBean>> mQuoteSent;

    public LiveData<List<DomainGoodsInfo.DataBean>> getQuoteReceived(String token) {
        quoteImpl.getReceivedQuote(token);
        return mQuoteReceived;
    }

    public LiveData<List<DomainGoodsInfo.DataBean>> getQuoteSent(String token) {
        quoteImpl.getSentQuote(token);
        return mQuoteSent;
    }

    public QuoteViewModel() {
        quoteImpl = new QuoteSectionImpl();
        quoteImpl.registerCallback(this);
    }

    @Override
    public void onQuoteReceivedLoaded(List<DomainGoodsInfo.DataBean> received) {
        mQuoteReceived = new MutableLiveData<>();
        mQuoteReceived.postValue(received);
    }

    @Override
    public void onQuoteSentLoaded(List<DomainGoodsInfo.DataBean> sent) {
        mQuoteSent = new MutableLiveData<>();
        mQuoteSent.postValue(sent);
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

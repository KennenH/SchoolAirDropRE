package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteReceiveItemBean;
import com.example.schoolairdroprefactoredition.presenter.IQuoteSectionsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

import java.util.Arrays;

public class QuoteSectionImpl implements IQuoteSectionsPresenter {

    private IQuoteSectionsCallback mCallback;

    @Override
    public void getQuote(/* 报价信息参数 */) {
        // 网络请求报价信息
        TestQuoteReceiveItemBean[] beans = new TestQuoteReceiveItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestQuoteReceiveItemBean();

        beans[0].setHandled(false);
        beans[1].setResult(TestQuoteReceiveItemBean.ACCEPT);
        beans[2].setResult(TestQuoteReceiveItemBean.ACCEPT);
        beans[3].setResult(TestQuoteReceiveItemBean.OUT_OF_DATE);
        beans[4].setResult(TestQuoteReceiveItemBean.REFUSE);
        beans[5].setResult(TestQuoteReceiveItemBean.OUT_OF_DATE);
        beans[6].setResult(TestQuoteReceiveItemBean.REFUSE);
        beans[7].setResult(TestQuoteReceiveItemBean.ACCEPT);
        beans[8].setResult(TestQuoteReceiveItemBean.OUT_OF_DATE);
        beans[9].setResult(TestQuoteReceiveItemBean.REFUSE);
        mCallback.onQuoteLoaded(Arrays.asList(beans));
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

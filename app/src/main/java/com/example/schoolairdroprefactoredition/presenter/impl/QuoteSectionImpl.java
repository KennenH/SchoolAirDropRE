package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;
import com.example.schoolairdroprefactoredition.presenter.IQuoteSectionsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

import java.util.Arrays;

public class QuoteSectionImpl implements IQuoteSectionsPresenter {

    private IQuoteSectionsCallback mCallback;

    @Override
    public void getReceivedQuoteSection(/* 报价信息参数 */) {
        // 网络请求报价信息
        TestQuoteSectionItemBean[] beans = new TestQuoteSectionItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestQuoteSectionItemBean();

        beans[0].setHandled(false);
        beans[1].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[2].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[3].setResult(TestQuoteSectionItemBean.OUT_OF_DATE);
        beans[4].setResult(TestQuoteSectionItemBean.REFUSE);
        beans[5].setHandled(false);
        beans[6].setResult(TestQuoteSectionItemBean.REFUSE);
        beans[7].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[8].setHandled(false);
        beans[9].setResult(TestQuoteSectionItemBean.REFUSE);
        Log.d("SectionImpl", "getReceivedQuoteSection");
        mCallback.onQuoteReceivedSectionLoaded(Arrays.asList(beans));
    }

    @Override
    public void getSentQuoteSection() {
        // 网络请求报价信息
        TestQuoteSectionItemBean[] beans = new TestQuoteSectionItemBean[10];
        for (int i = 0; i < 10; i++)
            beans[i] = new TestQuoteSectionItemBean();

        beans[0].setHandled(false);
        beans[1].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[2].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[3].setHandled(false);
        beans[4].setResult(TestQuoteSectionItemBean.REFUSE);
        beans[5].setResult(TestQuoteSectionItemBean.OUT_OF_DATE);
        beans[6].setResult(TestQuoteSectionItemBean.REFUSE);
        beans[7].setResult(TestQuoteSectionItemBean.ACCEPT);
        beans[8].setResult(TestQuoteSectionItemBean.OUT_OF_DATE);
        beans[9].setHandled(false);
        Log.d("SectionImpl", "getSentQuoteSection");
        mCallback.onQuoteSentSectionLoaded(Arrays.asList(beans));
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

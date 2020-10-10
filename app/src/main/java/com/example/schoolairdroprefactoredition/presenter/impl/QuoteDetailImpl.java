package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestQuoteDetailBean;
import com.example.schoolairdroprefactoredition.presenter.IQuoteDetailPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteDetailCallback;
import com.example.schoolairdroprefactoredition.ui.components.QuoteStatus;

import java.util.Calendar;
import java.util.Locale;

public class QuoteDetailImpl implements IQuoteDetailPresenter {

    private IQuoteDetailCallback mCallback;

    @Override
    public void getQuoteDetail(/* 报价编号 */) {
        // 以唯一标识报价单号为参数请求报价详情
        TestQuoteDetailBean bean = new TestQuoteDetailBean();
        bean.setTitle("拼夕夕正品iPhone20 骨折出售");
        bean.setQuoteStatus(QuoteStatus.EXPIRED);
        bean.setOriginPrice(1099);
        bean.setQuotePrice(999);
        bean.setOrderID("032485439085345213");
        Calendar now = Calendar.getInstance(Locale.CHINA);
        bean.setCreateTime(now);
        now.add(Calendar.DAY_OF_YEAR, 1);
        bean.setExpirationTime(now);

        if (mCallback != null)
            mCallback.onQuoteDetailLoaded(bean);
    }

    @Override
    public void registerCallback(IQuoteDetailCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(IQuoteDetailCallback callback) {
        mCallback = null;
    }
}

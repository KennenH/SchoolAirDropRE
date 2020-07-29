package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteDetailCallback;

public interface IQuoteDetailPresenter extends IBasePresenter<IQuoteDetailCallback> {
    /**
     * 以订单编号为参数请求报价详情
     */
    void getQuoteDetail(/* 订单编号 */);
}

package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

public interface IQuoteSectionsPresenter extends IBasePresenter<IQuoteSectionsCallback> {
    void getReceivedQuote(/* 报价订单号 */);

    void getSentQuote(/* 参数同上 */);
}

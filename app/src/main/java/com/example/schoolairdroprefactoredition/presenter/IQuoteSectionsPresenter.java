package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

public interface IQuoteSectionsPresenter extends IBasePresenter<IQuoteSectionsCallback> {
    void getReceivedQuoteSection(/* 报价订单号 */);

    void getSentQuoteSection(/* 参数同上 */);
}

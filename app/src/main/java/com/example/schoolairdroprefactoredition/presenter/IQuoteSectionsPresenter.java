package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IQuoteSectionsCallback;

public interface IQuoteSectionsPresenter extends IBasePresenter<IQuoteSectionsCallback> {
    void getQuote();

}

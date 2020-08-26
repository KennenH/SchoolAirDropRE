package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IUnselectedTransactionCallback;

public interface IUnselectedTransactionPresenter extends IBasePresenter<IUnselectedTransactionCallback> {
    void getUnselectedTransaction();
}

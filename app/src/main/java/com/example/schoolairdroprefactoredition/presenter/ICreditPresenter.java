package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ICreditCallback;

public interface ICreditPresenter extends IBasePresenter<ICreditCallback> {
    void getCredit();

    void getCreditHistory();
}

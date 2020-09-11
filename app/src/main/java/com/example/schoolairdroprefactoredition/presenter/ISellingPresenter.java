package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISellingCallback;

public interface ISellingPresenter extends IBasePresenter<ISellingCallback> {
    void getSelling(String token);
}

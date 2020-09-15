package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IBoughtCallback;

public interface IBoughtPresenter extends IBasePresenter<IBoughtCallback> {
    void getBoughtList(String token);
}

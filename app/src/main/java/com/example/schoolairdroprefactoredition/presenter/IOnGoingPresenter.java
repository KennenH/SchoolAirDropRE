package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IOnGoingCallback;

public interface IOnGoingPresenter extends IBasePresenter<IOnGoingCallback> {
    void getMyReceived(String token);

    void getMySent(String token);
}

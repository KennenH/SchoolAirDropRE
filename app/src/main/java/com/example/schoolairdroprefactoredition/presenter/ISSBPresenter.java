package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;

public interface ISSBPresenter extends IBasePresenter<ISSBCallback> {
    void getSoldList(String token);

    void getBoughtList(String token);

    void getSellingList(String token);
}

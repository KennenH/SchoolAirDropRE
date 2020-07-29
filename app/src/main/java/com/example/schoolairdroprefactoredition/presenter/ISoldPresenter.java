package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISoldCallback;

public interface ISoldPresenter extends IBasePresenter<ISoldCallback> {
    void getSoldList();
}

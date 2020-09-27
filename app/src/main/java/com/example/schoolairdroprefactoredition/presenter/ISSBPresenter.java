package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;

public interface ISSBPresenter extends IBasePresenter<ISSBCallback> {
    void getSoldList(String token, int page);

    void getBoughtList(String token, int page);

    void getSellingList(String token, int page);

    void unListItem(String token, String goodsID);
}

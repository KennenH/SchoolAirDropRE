package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;

public interface IHomePostPresenter extends IBasePresenter<IHomeNewsCallback> {
    void getNews(int page);
}

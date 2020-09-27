package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;

public interface IHomeNewsPresenter extends IBasePresenter<IHomeNewsCallback> {
    void getNews(int page);
}

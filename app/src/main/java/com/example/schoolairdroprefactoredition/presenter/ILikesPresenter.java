package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ILikesCallback;

public interface ILikesPresenter extends IBasePresenter<ILikesCallback> {
    void getLikes(String token);
}

package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ICacheCallback;

public interface ICachePresenter extends IBasePresenter<ICacheCallback> {
    /**
     * 用户信息缓存
     */
    void getUserInfoCache();

    /**
     * 用户token缓存
     */
    void getTokenCache();
}

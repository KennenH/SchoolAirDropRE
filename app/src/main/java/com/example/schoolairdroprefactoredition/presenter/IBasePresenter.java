package com.example.schoolairdroprefactoredition.presenter;

public interface IBasePresenter<T> {
    /**
     * 注册接口
     * @param callback
     */
    void registerCallback(T callback);

    /**
     * 注销接口
     * @param callback
     */
    void unregisterCallback(T callback);
}

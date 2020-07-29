package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;

public interface ISettingsPresenter extends IBasePresenter<ISettingsCallback> {
    void getPublicKey();

    void postAlipayIDRSA(String publicKey, String alipayID) throws Exception;
}

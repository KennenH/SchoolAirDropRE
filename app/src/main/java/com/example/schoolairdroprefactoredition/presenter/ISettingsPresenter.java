package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISettingsCallback;

public interface ISettingsPresenter extends IBasePresenter<ISettingsCallback> {
    void getPublicKey();

    void postAlipayIDRSA(String sessionID, String grantType, String clientID, String clientSecret, String rawAlipay, String publicKey) throws Exception;
}

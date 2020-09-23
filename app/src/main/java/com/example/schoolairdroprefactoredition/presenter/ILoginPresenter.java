package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ILoginCallback;

public interface ILoginPresenter extends IBasePresenter<ILoginCallback> {
    void getPublicKey();

    void postAlipayIDRSA(String cookie, String rawAlipay, String publicKey) throws Exception;

    void getUserInfo(String token);

    void logout();
}

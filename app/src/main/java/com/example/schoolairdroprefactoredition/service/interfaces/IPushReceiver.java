package com.example.schoolairdroprefactoredition.service.interfaces;

public interface IPushReceiver {
    /**
     * 收到报价通知
     */
    void onReceiveQuote();

    /**
     * 收到物品审核通知
     */
    void onReceiveNewItemPass();
}

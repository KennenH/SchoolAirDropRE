package com.example.schoolairdroprefactoredition.service.interfaces;

public interface IOverlayWindowCommunication {
    /**
     * 收到新的报价
     */
    void onQuotePushReceived();

    /**
     * 收到物品审核通知
     */
    void onItemReviewPushReceived();
}

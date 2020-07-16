package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainChatBean;

public interface IChatCallback {
    /**
     * 数据加载成功
     */
    void onHistoriesLoaded(DomainChatBean data);

    /**
     * 没有更多历史聊天记录
     */
    void onHistoriesEmpty();

    /**
     * 数据加载失败
     */
    void onHistoriesLoadFailed();
}

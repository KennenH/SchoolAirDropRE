package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.IChatCallback;

public interface IChatPresenter extends IBasePresenter<IChatCallback> {
    /**
     * 加载聊天历史记录
     */
    void getChatHistories();

    /**
     * 实时获取消息
     * todo 应该作为服务
     */
    void getMessage();
}

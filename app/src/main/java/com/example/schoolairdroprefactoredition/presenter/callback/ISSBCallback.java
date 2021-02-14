package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainPostInfo;
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing;

public interface ISSBCallback extends IBaseCallback {
    // 获取在售
    void onSellingLoaded(DomainPurchasing selling);

    // 下架物品
    void onUnListItemSuccess();

    // 操作失败
    void onActionFailed();

    // 获取帖子
    void onPostLoaded(DomainPostInfo postInfo);

    // 删除帖子
    void onDeletePostSuccess();
}

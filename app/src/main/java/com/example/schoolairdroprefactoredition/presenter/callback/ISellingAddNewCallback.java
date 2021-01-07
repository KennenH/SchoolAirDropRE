package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache;
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache;
import com.example.schoolairdroprefactoredition.domain.DomainResult;

public interface ISellingAddNewCallback {

    /**
     * 添加新物品结果回调
     */
    void onSubmitItemResult(DomainResult result);

    /**
     * 修改物品信息回调
     */
    void onUpdateItemResult(DomainResult result);

    /**
     * 添加新物品草稿恢复回调
     */
    void onItemDraftRestored(NewItemDraftCache draftCache);

    /**
     * 上传新帖子结果回调
     */
    void onSubmitPostItemResult(DomainResult result);

    /**
     * 新帖子草稿恢复回调
     */
    void onPostDraftRestored(NewPostDraftCache draftCache);

    /**
     * 新增物品时出错
     */
    void onAddNewItemError();

    /**
     * 修改物品时出错
     */
    void onModifyInfoError();

    /**
     * 发帖子时出错
     */
    void onAddNewPostError();

    /**
     * 其他错误
     */
    void onOtherError();
}

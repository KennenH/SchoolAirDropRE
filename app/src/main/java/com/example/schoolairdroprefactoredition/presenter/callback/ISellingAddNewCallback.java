package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewDraftCache;

public interface ISellingAddNewCallback extends IBaseCallback {
    /**
     * 添加新物品结果回调
     */
    void onSubmitResult(DomainResult result);

    /**
     * 添加新物品草稿恢复回调
     */
    void onDraftRecovered(AddNewDraftCache draftCache);
}

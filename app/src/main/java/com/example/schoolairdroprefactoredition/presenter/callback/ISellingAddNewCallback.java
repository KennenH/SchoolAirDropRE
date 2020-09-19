package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainResult;

public interface ISellingAddNewCallback extends IBaseCallback {
    void onResult(DomainResult result);
}

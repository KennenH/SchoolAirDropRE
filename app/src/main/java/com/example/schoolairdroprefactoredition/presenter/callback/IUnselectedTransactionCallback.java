package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainUnselectedTransaction;

import java.util.List;

public interface IUnselectedTransactionCallback extends IBaseCallback {
    void onTransactionLoaded(List<DomainUnselectedTransaction> list);

    void onNoTransaction();
}

package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;

public interface IBoughtCallback extends IBaseCallback {
    void onBoughtListLoaded(DomainGoodsInfo beans);
}

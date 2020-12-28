package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;

public interface ISellingCallback extends IBaseCallback {
    void onSellingLoaded(DomainGoodsInfo beans);
}

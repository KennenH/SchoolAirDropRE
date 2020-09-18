package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;

public interface ISSBCallback extends IBaseCallback {
    // 在售
    void onSellingListLoaded(DomainGoodsInfo selling);

    // 已售
    void onSoldListLoaded(DomainGoodsInfo sold);

    // 已购
    void onBoughtListLoaded(DomainGoodsInfo bought);
}

package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;

import java.util.List;

public interface ISoldCallback extends IBaseCallback {
    void onSoldListLoaded(DomainGoodsInfo beans);
}

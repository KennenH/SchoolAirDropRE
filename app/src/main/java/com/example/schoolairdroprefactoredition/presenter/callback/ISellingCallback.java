package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;

import java.util.List;

public interface ISellingCallback extends IBaseCallback {
    void onSellingListLoaded(List<TestSSBItemBean> beans);
}

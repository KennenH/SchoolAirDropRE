package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;

import java.util.List;

public interface IBoughtCallback extends IBaseCallback {
    void onBoughtListLoaded(List<TestSSBItemBean> beans);
}

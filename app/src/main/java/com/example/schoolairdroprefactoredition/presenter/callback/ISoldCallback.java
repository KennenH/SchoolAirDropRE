package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;

import java.util.List;

public interface ISoldCallback extends IBaseCallback {
    void onSoldListLoaded(List<TestSSBItemBean> beans);
}

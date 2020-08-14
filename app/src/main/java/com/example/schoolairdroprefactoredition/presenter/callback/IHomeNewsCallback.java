package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import java.util.List;

public interface IHomeNewsCallback extends IBaseCallback {
    void onNewsLoaded(List<TestNewsItemBean> data);
}

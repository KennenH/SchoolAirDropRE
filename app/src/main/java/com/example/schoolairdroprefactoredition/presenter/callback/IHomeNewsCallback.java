package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import java.util.List;

public interface IHomeNewsCallback {
    /**
     * 新闻加载完毕
     */
    void onNewsLoaded(List<TestNewsItemBean> data);

    void onNewsLoading();

    void onNewsEmpty();
}

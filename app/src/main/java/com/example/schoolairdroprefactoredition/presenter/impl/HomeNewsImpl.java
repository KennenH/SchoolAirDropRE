package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;
import com.example.schoolairdroprefactoredition.presenter.IHomeNewsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;

import java.util.Arrays;

public class HomeNewsImpl implements IHomeNewsPresenter {

    private IHomeNewsCallback mCallback = null;

    /**
     * 请求最新消息的数据
     */
    @Override
    public void getNews(int page) {

        TestNewsItemBean[] data = new TestNewsItemBean[12];
        for (int i = 0; i < 12; i++) {
            data[i] = new TestNewsItemBean();
            if (i % 4 == 0) {
                data[i].setTitle("快速上手");
                data[i].setType(HomeNewsRecyclerAdapter.TYPE_TWO);
            } else {
                data[i].setTitle("校园空投校园内测开始啦!进来解锁新姿势!");
            }
        }
        mCallback.onNewsLoaded(Arrays.asList(data));
    }

    @Override
    public void registerCallback(IHomeNewsCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeNewsCallback callback) {
        mCallback = null;
    }
}

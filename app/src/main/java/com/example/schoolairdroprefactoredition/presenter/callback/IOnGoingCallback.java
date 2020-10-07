package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;

import java.util.List;

public interface IOnGoingCallback extends IBaseCallback{
    void onUnPaidLoaded(List<TestOnGoingBean> data);

    void onUnDeliveredLoaded(List<TestOnGoingBean> data);
}

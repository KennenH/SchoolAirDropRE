package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

import java.util.List;

public interface IHomeNewsCallback extends IBaseCallback {
    void onNewsLoaded(List<BaseHomeNewsEntity> data);
}

package com.example.schoolairdroprefactoredition.scene.main.my;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.MyImpl;

public class MyViewModel extends BaseStateViewModel implements IMyCallback {

    private final MyImpl mImpl;

    private final MutableLiveData<DomainUserInfo> mUserInfo = new MutableLiveData<>();

    public MyViewModel() {
        mImpl = new MyImpl();
        mImpl.registerCallback(this);
    }

    public LiveData<DomainUserInfo> getUserInfo(String token) {
        mImpl.getUserInfo(token);
        return mUserInfo;
    }

    @Override
    public void onUserInfoLoaded(DomainUserInfo data) {
        mUserInfo.postValue(data);
    }

    @Override
    protected void onCleared() {
        mImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        if (mOnRequestListener != null)
            mOnRequestListener.onError();
    }

}
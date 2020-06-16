package com.example.schoolairdroprefactoredition.fragment.my;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestUserInfoBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.MyImpl;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class MyViewModel extends ViewModel implements IMyCallback {

    private MyImpl mImpl;

    private MutableLiveData<TestUserInfoBean> mUserInfo;

    public MyViewModel() {
        mImpl = new MyImpl();
        mImpl.registerCallback(this);
        mImpl.getUserInfo();

    }

    public LiveData<TestUserInfoBean> getUserInfo() {
        return mUserInfo;
    }

    @Override
    public void onUserInfoLoaded(TestUserInfoBean data) {
        mUserInfo = new MutableLiveData<>();
        mUserInfo.setValue(data);
    }

    @Override
    protected void onCleared() {
        mImpl.unregisterCallback(this);
    }
}
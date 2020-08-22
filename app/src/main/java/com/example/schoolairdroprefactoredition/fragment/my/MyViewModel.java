package com.example.schoolairdroprefactoredition.fragment.my;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.fragment.home.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestUserInfoBean;
import com.example.schoolairdroprefactoredition.presenter.callback.IMyCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.MyImpl;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class MyViewModel extends BaseChildFragmentViewModel implements IMyCallback {

    private MyImpl mImpl;

    private MutableLiveData<DomainGetUserInfo> mUserInfo = new MutableLiveData<>();

    public MyViewModel() {
        mImpl = new MyImpl();
        mImpl.registerCallback(this);
    }

    public LiveData<DomainGetUserInfo> getUserInfo(String token) {
        mImpl.getUserInfo(token);
        return mUserInfo;
    }

    @Override
    public void onUserInfoLoaded(DomainGetUserInfo data) {
        mUserInfo.postValue(data);
    }

    @Override
    protected void onCleared() {
        mImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }
}
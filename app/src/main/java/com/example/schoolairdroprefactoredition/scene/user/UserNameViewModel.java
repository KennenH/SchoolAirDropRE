package com.example.schoolairdroprefactoredition.scene.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IUserNameCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserNameImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class UserNameViewModel extends BaseStateViewModel implements IUserNameCallback {

    private MutableLiveData<Boolean> mRenameResult = new MutableLiveData<>();

    private UserNameImpl userNameImpl;

    public UserNameViewModel() {
        userNameImpl = new UserNameImpl();
        userNameImpl.registerCallback(this);
    }

    public LiveData<Boolean> rename(String token, String name) {
        userNameImpl.rename(token, name);
        return mRenameResult;
    }

    @Override
    public void onResult(boolean success) {
        mRenameResult.postValue(success);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        userNameImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

    @Override
    public void onLoading() {

    }
}

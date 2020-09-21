package com.example.schoolairdroprefactoredition.scene.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IResultCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserNameImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class ResultViewModel extends BaseStateViewModel implements IResultCallback {

    private MutableLiveData<Boolean> mRenameResult = new MutableLiveData<>();

    private UserNameImpl userNameImpl;

    public ResultViewModel() {
        userNameImpl = new UserNameImpl();
        userNameImpl.registerCallback(this);
    }

    public LiveData<Boolean> rename(String token, String name) {
        userNameImpl.rename(token, name);
        return mRenameResult;
    }

    @Override
    public void onSuccess() {
        mRenameResult.postValue(true);
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

}

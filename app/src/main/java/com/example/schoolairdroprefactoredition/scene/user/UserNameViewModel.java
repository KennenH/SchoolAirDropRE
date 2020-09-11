package com.example.schoolairdroprefactoredition.scene.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.presenter.callback.IUserNameCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserNameImpl;

public class UserNameViewModel extends ViewModel implements IUserNameCallback {

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
}

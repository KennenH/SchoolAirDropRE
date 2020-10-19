package com.example.schoolairdroprefactoredition.scene.user.fragment;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateAndroidViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserAvatarImpl;

public class UserAvatarViewModel extends BaseStateAndroidViewModel implements IUserAvatarCallback {

    private MutableLiveData<DomainAvatarUpdateResult> mUpdateCallback = new MutableLiveData<>();

    private UserAvatarImpl userAvatarImpl;

    public UserAvatarViewModel(@NonNull Application application) {
        super(application);
        userAvatarImpl = new UserAvatarImpl();
        userAvatarImpl.registerCallback(this);
    }

    public LiveData<DomainAvatarUpdateResult> updateAvatar(String token, String img) {
        userAvatarImpl.updateAvatar(getApplication(), token, img);
        return mUpdateCallback;
    }


    @Override
    public void onUpdateSuccess(DomainAvatarUpdateResult result) {
        mUpdateCallback.postValue(result);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }


}
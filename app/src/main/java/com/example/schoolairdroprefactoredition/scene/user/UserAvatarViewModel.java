package com.example.schoolairdroprefactoredition.scene.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserAvatarImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateAndroidViewModel;

public class UserAvatarViewModel extends BaseStateAndroidViewModel implements IUserAvatarCallback {

    private final MutableLiveData<DomainAvatarUpdateResult> mUpdateCallback = new MutableLiveData<>();

    private final UserAvatarImpl userAvatarImpl;

    public UserAvatarViewModel(@NonNull Application application) {
        super(application);
        userAvatarImpl = UserAvatarImpl.getInstance();
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
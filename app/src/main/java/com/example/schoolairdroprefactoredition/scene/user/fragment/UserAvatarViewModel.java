package com.example.schoolairdroprefactoredition.scene.user.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserAvatarImpl;

public class UserAvatarViewModel extends BaseStateViewModel implements IUserAvatarCallback {

    private MutableLiveData<DomainAvatarUpdateResult> mUpdateCallback = new MutableLiveData<>();

    private UserAvatarImpl userAvatarImpl;

    public UserAvatarViewModel() {
        userAvatarImpl = new UserAvatarImpl();
        userAvatarImpl.registerCallback(this);
    }

    public LiveData<DomainAvatarUpdateResult> updateAvatar(String token, String img) {
        userAvatarImpl.updateAvatar(token, img);
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
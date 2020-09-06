package com.example.schoolairdroprefactoredition.scene.main.my.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdate;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.presenter.callback.IUserAvatarCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.UserAvatarImpl;

public class UserAvatarViewModel extends BaseChildFragmentViewModel implements IUserAvatarCallback {

    private MutableLiveData<DomainAvatarUpdate> mUpdateCallback = new MutableLiveData<>();

    private UserAvatarImpl userAvatarImpl;

    public LiveData<DomainAvatarUpdate> updateAvatar(String img, String uid) {
        userAvatarImpl.sendAvatar(img, uid);
        return mUpdateCallback;
    }

    public UserAvatarViewModel() {
        userAvatarImpl = new UserAvatarImpl();
        userAvatarImpl.registerCallback(this);
    }

    @Override
    public void onSent(DomainAvatarUpdate result) {
        mUpdateCallback.postValue(result);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }

    @Override
    public void onLoading() {
        mOnRequestListener.onLoading();
    }


}
package com.example.schoolairdroprefactoredition.scene.tab.likes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.ILikesCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.LikesImpl;

public class LikesViewModel extends ViewModel implements ILikesCallback {

    private LikesImpl likesImpl;

    private MutableLiveData<DomainGoodsInfo> mLikes = new MutableLiveData<>();

    public LikesViewModel() {
        likesImpl = new LikesImpl();
        likesImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getLikes(String token) {
        likesImpl.getLikes(token);
        return mLikes;
    }

    @Override
    public void onLikesLoaded(DomainGoodsInfo likes) {
        mLikes.postValue(likes);
    }

    @Override
    public void onError() {

    }
}

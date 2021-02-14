package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainPostInfo;
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing;
import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SSBImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class SSBViewModel extends BaseStateViewModel implements ISSBCallback {

    private int sellingPage = 0;
    private int postsPage = 0;

    private final SSBImpl ssbImpl;

    private final MutableLiveData<DomainPurchasing> mSelling = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mUnListItemResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mUpdateItemResult = new MutableLiveData<>();

    private final MutableLiveData<DomainPostInfo> mPosts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDeletePostResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mUpdatePostResult = new MutableLiveData<>();

    private OnSSBActionListener mOnSSBActionListener;

    public SSBViewModel() {
        ssbImpl = SSBImpl.getInstance();
        ssbImpl.registerCallback(this);
    }

    public LiveData<DomainPurchasing> getSelling(int userID) {
        ssbImpl.getSellingList(userID);
        return mSelling;
    }

    public LiveData<Boolean> unListItem(String token, String goodsID) {
        ssbImpl.unListItem(token, goodsID);
        return mUnListItemResult;
    }

    public LiveData<DomainPurchasing> getSellingByID(int userID) {
        ssbImpl.getSellingByUID(userID);
        return mSelling;
    }

    @Override
    public void onSellingLoaded(DomainPurchasing selling) {
        mSelling.postValue(selling);
    }

    @Override
    public void onUnListItemSuccess() {
        mUnListItemResult.postValue(true);
    }

    @Override
    public void onActionFailed() {
        if (mOnSSBActionListener != null)
            mOnSSBActionListener.onActionFailed();
    }

    @Override
    public void onPostLoaded(DomainPostInfo postInfo) {
        mPosts.postValue(postInfo);
    }

    @Override
    public void onDeletePostSuccess() {
        mDeletePostResult.postValue(true);
    }

    @Override
    public void onError() {
        if (mOnRequestListener != null)
            mOnRequestListener.onError();
    }

    public interface OnSSBActionListener {
        void onActionFailed();
    }

    public void setOnSSBActionListener(OnSSBActionListener listener) {
        mOnSSBActionListener = listener;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ssbImpl.unregisterCallback(this);
    }
}

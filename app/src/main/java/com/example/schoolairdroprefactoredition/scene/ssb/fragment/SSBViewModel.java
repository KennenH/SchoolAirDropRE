package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SSBImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class SSBViewModel extends BaseStateViewModel implements ISSBCallback {

    private int sellingPage = 0;
    private int soldPage = 0;
    private int boughtPage = 0;

    private SSBImpl ssbImpl;

    private MutableLiveData<DomainGoodsInfo> mSellingBeans = new MutableLiveData<>();
    private MutableLiveData<DomainGoodsInfo> mBoughtBeans = new MutableLiveData<>();
    private MutableLiveData<DomainGoodsInfo> mSoldBeans = new MutableLiveData<>();
    private MutableLiveData<Boolean> mUnListItemResult = new MutableLiveData<>();

    private OnSSBActionListener mOnSSBActionListener;

    public SSBViewModel() {
        ssbImpl = new SSBImpl();
        ssbImpl.registerCallback(this);
    }

    public LiveData<DomainGoodsInfo> getSelling(String token) {
        ssbImpl.getSellingList(token, 1);
        return mSellingBeans;
    }

    public LiveData<DomainGoodsInfo> getBought(String token) {
        ssbImpl.getBoughtList(token, 1);
        return mBoughtBeans;
    }

    public LiveData<DomainGoodsInfo> getSold(String token) {
        ssbImpl.getSoldList(token, 1);
        return mSoldBeans;
    }

    public LiveData<Boolean> unListItem(String token, String goodsID) {
        ssbImpl.unListItem(token, goodsID);
        return mUnListItemResult;
    }

    public LiveData<DomainGoodsInfo> getSellingByID(int userID) {
        ssbImpl.getSellingByUID(userID);
        return mSellingBeans;
    }

    @Override
    public void onSellingListLoaded(DomainGoodsInfo selling) {
        mSellingBeans.postValue(selling);
    }

    @Override
    public void onSoldListLoaded(DomainGoodsInfo sold) {
        mSoldBeans.postValue(sold);
    }

    @Override
    public void onBoughtListLoaded(DomainGoodsInfo bought) {
        mBoughtBeans.postValue(bought);
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

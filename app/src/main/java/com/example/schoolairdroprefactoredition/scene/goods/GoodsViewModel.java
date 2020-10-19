package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.GoodsImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class GoodsViewModel extends BaseStateViewModel implements IGoodsCallback {

    private GoodsImpl goodsImpl;

    private MutableLiveData<Boolean> mQuoteResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFavoriteResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> mUnFavorResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsFavoredResult = new MutableLiveData<>();

    public GoodsViewModel() {
        goodsImpl = new GoodsImpl();
        goodsImpl.registerCallback(this);
    }

    public LiveData<Boolean> quoteRequest(String token, String goodsID, String quotePrice) {
        goodsImpl.quoteItem(token, goodsID, quotePrice);
        return mQuoteResult;
    }

    public LiveData<Boolean> favoriteItem(String token, String goodsID) {
        goodsImpl.favoriteItem(token, goodsID);
        return mFavoriteResult;
    }

    public LiveData<Boolean> isItemFavored(String token, String goodsID) {
        goodsImpl.isItemFavored(token, goodsID);
        return mIsFavoredResult;
    }

    public LiveData<Boolean> unFavorItem(String token, String goodsID) {
        goodsImpl.unFavorItem(token, goodsID);
        return mUnFavorResult;
    }

    @Override
    public void onQuoteSuccess() {
        mQuoteResult.postValue(true);
    }

    @Override
    public void onFavoriteSuccess() {
        mFavoriteResult.postValue(true);
    }

    @Override
    public void onIsFavorGet(boolean isFavor) {
        mIsFavoredResult.postValue(isFavor);
    }

    @Override
    public void onUnFavorSuccess() {
        mUnFavorResult.postValue(true);
    }


    @Override
    public void onError() {
        if (mOnRequestListener != null)
            mOnRequestListener.onError();
    }
}

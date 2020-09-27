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

    public GoodsViewModel() {
        goodsImpl = new GoodsImpl();
        goodsImpl.registerCallback(this);
    }

    public LiveData<Boolean> quoteRequest(String token, String goodsID, String quotePrice) {
        goodsImpl.quoteRequest(token, goodsID, quotePrice);
        return mQuoteResult;
    }

    public LiveData<Boolean> favoriteItem(String token, String goodsID) {
        goodsImpl.favorite(token, goodsID);
        return mFavoriteResult;
    }

    @Override
    public void onQuoteSuccess() {
        mQuoteResult.postValue(true);
    }

    @Override
    public void onFavoriteSuccess() {

    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}

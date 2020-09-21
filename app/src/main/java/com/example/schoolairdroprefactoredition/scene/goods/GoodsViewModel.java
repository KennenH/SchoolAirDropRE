package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.GoodsImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class GoodsViewModel extends BaseStateViewModel implements IGoodsCallback {

    private GoodsImpl goodsImpl;

    private MutableLiveData<Boolean> mResult = new MutableLiveData<>();

    public GoodsViewModel() {
        goodsImpl = new GoodsImpl();
        goodsImpl.registerCallback(this);
    }

    public LiveData<Boolean> quoteRequest(String token, String goodsID, String quotePrice) {
        goodsImpl.quoteRequest(token, goodsID, quotePrice);
        return mResult;
    }

    @Override
    public void onQuoteSuccess() {
        mResult.postValue(true);
    }

    @Override
    public void onFavoriteSuccess() {

    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}

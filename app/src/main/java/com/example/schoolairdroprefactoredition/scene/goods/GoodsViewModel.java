package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IGoodsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.GoodsImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

public class GoodsViewModel extends BaseStateViewModel implements IGoodsCallback {

    private final GoodsImpl goodsImpl;

    private MutableLiveData<Boolean> mQuoteResult;

    public GoodsViewModel() {
        goodsImpl = new GoodsImpl();
        goodsImpl.registerCallback(this);
    }

    public LiveData<Boolean> quoteRequest(String token, String goodsID, String quotePrice) {
        goodsImpl.quoteItem(token, goodsID, quotePrice);
        return mQuoteResult;
    }

    @Override
    public void onQuoteSuccess() {
        mQuoteResult = new MutableLiveData<>();
        mQuoteResult.postValue(true);
    }

    @Override
    public void onError() {
        if (mOnRequestListener != null)
            mOnRequestListener.onError();
    }
}

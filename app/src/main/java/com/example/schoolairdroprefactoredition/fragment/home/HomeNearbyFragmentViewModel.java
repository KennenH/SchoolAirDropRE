package com.example.schoolairdroprefactoredition.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeGoodsInfoImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeNearbyFragmentViewModel extends ViewModel implements IHomeGoodsInfoCallback {

    private HomeGoodsInfoImpl mHomeImpl;

    private MutableLiveData<List<TestGoodsItemBean>> mGoodsInfo;

    public HomeNearbyFragmentViewModel() {
        mHomeImpl = new HomeGoodsInfoImpl();
        mHomeImpl.registerCallback(this);
    }

    public LiveData<List<TestGoodsItemBean>> getGoodsInfo() {
        mHomeImpl.getNearbyGoodsInfo(ConstantUtil.DATA_FETCH_DEFAULT_SIZE);
        return mGoodsInfo;
    }

    @Override
    public void onGoodsInfoLoading() {
        //todo 附近商品尚在加载
    }

    @Override
    public void onGoodsInfoLoaded(List<TestGoodsItemBean> domainGoodsInfo) {
        if (mGoodsInfo == null)
            mGoodsInfo = new MutableLiveData<>();
        mGoodsInfo.setValue(domainGoodsInfo);
    }

    @Override
    public void onGoodsInfoEmpty() {
        if (mGoodsInfo == null)
            mGoodsInfo = new MutableLiveData<>();
        mGoodsInfo.setValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }
}

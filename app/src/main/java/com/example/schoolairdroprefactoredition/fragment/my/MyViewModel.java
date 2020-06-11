package com.example.schoolairdroprefactoredition.fragment.my;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.utils.NumberUtil;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> mName;
    private MutableLiveData<String> mSelling;
    private MutableLiveData<String> mSold;
    private MutableLiveData<String> mPurchased;

    public LiveData<String> getName() {
        return mName;
    }

    public LiveData<String> getSelling() {
        return mSelling;
    }

    public LiveData<String> getSold() {
        return mSold;
    }

    public LiveData<String> getPurchased() {
        return mPurchased;
    }

    public MyViewModel() {
        getOnlineName();
        getOnlineSelling();
        getOnlineSold();
        getOnlinePurchased();
    }

    private void getOnlineName() {
        mName = new MutableLiveData<>();
        mName.setValue("奥利给");
    }

    private void getOnlineSelling() {
        mSelling = new MutableLiveData<>();
        mSelling.setValue(NumberUtil.num2StringWithUnit(4));
    }

    private void getOnlineSold() {
        mSold = new MutableLiveData<>();
        mSold.setValue(NumberUtil.num2StringWithUnit(5));
    }

    private void getOnlinePurchased() {
        mPurchased = new MutableLiveData<>();
        mPurchased.setValue(NumberUtil.num2StringWithUnit(13));
    }
}
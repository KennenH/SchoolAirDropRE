package com.example.schoolairdroprefactoredition.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;

public class MainViewModel extends ViewModel {

    private MutableLiveData<DomainGetUserInfo> mUserInfo = new MutableLiveData<>();

}

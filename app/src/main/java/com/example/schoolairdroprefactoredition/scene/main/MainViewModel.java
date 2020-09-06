package com.example.schoolairdroprefactoredition.scene.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;

public class MainViewModel extends ViewModel {

    private MutableLiveData<DomainGetUserInfo> mUserInfo = new MutableLiveData<>();

}

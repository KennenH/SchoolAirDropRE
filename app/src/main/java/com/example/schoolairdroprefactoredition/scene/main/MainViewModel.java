package com.example.schoolairdroprefactoredition.scene.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public class MainViewModel extends ViewModel {

    private MutableLiveData<DomainUserInfo> mUserInfo = new MutableLiveData<>();

}

package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.DomainBaseUserInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.UserRepository

class UserViewModel : ViewModel() {

    private val userRepository by lazy {
        UserRepository.getInstance()
    }

    val userInfoLoadState = MutableLiveData<LoadState>()

    private val userBaseInfo = MutableLiveData<DomainBaseUserInfo>()

    fun getUserBaseInfoByID(userID: Int): LiveData<DomainBaseUserInfo> {
        userRepository.getUserInfo(userID) { success, response ->
            if (success) {
                userBaseInfo.postValue(response)
                userInfoLoadState.value = LoadState.SUCCESS
            } else {
                userInfoLoadState.value = LoadState.ERROR
            }
        }
        return userBaseInfo
    }
}
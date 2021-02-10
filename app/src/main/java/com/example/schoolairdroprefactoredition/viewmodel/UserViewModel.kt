package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.UserRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository by lazy {
        UserRepository.getInstance()
    }

    val userInfoLoadState = MutableLiveData<LoadState>()

    private val userBaseInfo = MutableLiveData<DomainUserInfo.DataBean>()

    fun getUserBaseInfoByID(userID: Int): LiveData<DomainUserInfo.DataBean> {
        viewModelScope.launch {
            userRepository.getUserInfoById(userID) { success, response ->
                if (success) {
                    userBaseInfo.postValue(response)
                    userInfoLoadState.value = LoadState.SUCCESS
                } else {
                    userInfoLoadState.value = LoadState.ERROR
                }
            }
        }
        return userBaseInfo
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
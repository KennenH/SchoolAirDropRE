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

    private val userBaseInfo = MutableLiveData<DomainUserInfo.DataBean>()

    /**
     * 使用user id获取用户基本信息
     * todo 用户信息获取成功后应该进行缓存
     */
    fun getUserBaseInfoByID(userID: Int): LiveData<DomainUserInfo.DataBean?> {
        viewModelScope.launch {
            userRepository.getUserInfoById(userID) { success, response ->
                if (success) {
                    userBaseInfo.postValue(response)
                } else {
                    userBaseInfo.postValue(null)
                }
            }
        }
        return userBaseInfo
    }
}
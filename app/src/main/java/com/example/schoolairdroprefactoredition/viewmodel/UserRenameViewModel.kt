package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.repository.UserNameRepository
import kotlinx.coroutines.launch

class UserRenameViewModel : ViewModel() {

    private val mRenameResult = MutableLiveData<Boolean>()

    private val userNameRepository by lazy {
        UserNameRepository.getInstance()
    }

    fun rename(token: String, name: String): LiveData<Boolean> {
        viewModelScope.launch {
            userNameRepository.rename(token, name) {
                mRenameResult.postValue(it)
            }
        }
        return mRenameResult
    }
}
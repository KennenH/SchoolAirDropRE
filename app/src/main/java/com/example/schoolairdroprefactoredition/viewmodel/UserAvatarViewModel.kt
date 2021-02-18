package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.repository.UserAvatarRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class UserAvatarViewModel(application: Application) : AndroidViewModel(application) {

    private val updateLiveData = MutableLiveData<String?>()

    private val userAvatarRepository by lazy {
        UserAvatarRepository.getInstance()
    }

    private val uploadRepository by lazy {
        UploadRepository.getInstance()
    }

    fun updateAvatar(token: String?, avatarLocalPath: String): LiveData<String?> {
        if (token != null) {
            viewModelScope.launch {
                val array = ArrayList<String>(1).also { it.add(avatarLocalPath) }
                uploadRepository.doUpload(token, array, ConstantUtil.UPLOAD_TYPE_AVATAR) {
                    if (it != null) {
                        updateLiveData.postValue(it[0])
                    } else {
                        updateLiveData.postValue(null)
                    }
                }
            }
        } else {
            updateLiveData.postValue(null)
        }
        return updateLiveData
    }
}
package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.repository.UserAvatarRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.launch

class UserAvatarViewModel(application: Application) : AndroidViewModel(application) {

    private val userAvatarRepository by lazy {
        UserAvatarRepository.getInstance()
    }

    private val uploadRepository by lazy {
        UploadRepository.getInstance()
    }

    /**
     * 更新用户头像
     */
    fun updateAvatar(token: String?, avatarLocalPath: String): LiveData<String?> {
        val updateLiveData = MutableLiveData<String?>()
        if (token != null) {
            viewModelScope.launch {
                val localPath = ArrayList<String>(1).also { it.add(avatarLocalPath) }
                // 上传图片至七牛云
                uploadRepository.upload(token, localPath, ConstantUtil.UPLOAD_TYPE_AVATAR) { taskAndKeys ->
                    if (taskAndKeys != null) {
                        userAvatarRepository.updateAvatar(token, taskAndKeys.taskId, taskAndKeys.keys[0]) {
                            updateLiveData.postValue(it)
                        }
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
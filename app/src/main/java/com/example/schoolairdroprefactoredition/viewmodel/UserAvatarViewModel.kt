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
                // 上传图片至七牛云
                uploadRepository.upload(token, array, ConstantUtil.UPLOAD_TYPE_AVATAR) {
                    if (it != null) {
                        // 修改服务器上用户的头像路径
                        userAvatarRepository.updateAvatar(token, it[0]) { updateResult ->
                            if (updateResult) {
                                updateLiveData.postValue(it[0])
                            } else {
                                updateLiveData.postValue(null)
                            }
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
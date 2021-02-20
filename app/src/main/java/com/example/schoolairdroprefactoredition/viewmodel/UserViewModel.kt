package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UserRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class UserViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private val userRepository by lazy {
        UserRepository.getInstance()
    }

    private val userBaseInfo = MutableLiveData<DomainUserInfo.DataBean>()

    /**
     * 使用user id获取用户基本信息
     */
    fun getUserBaseInfoByID(userID: Int): LiveData<DomainUserInfo.DataBean?> {
        viewModelScope.launch {
            // 先用户信息获取缓存显示
            databaseRepository.getUserCache(userID)?.let { cache ->
                userBaseInfo.value = DomainUserInfo.DataBean().also {
                    it.userId = cache.user_id
                    it.userAvatar = cache.user_avatar
                    it.userName = cache.user_name
                    it.userGoodsOnSaleCount = cache.user_goods_count ?: 0
                    it.userContactCount = cache.user_post_count ?: 0
                }
            }
            // 再获取网络信息
            userRepository.getUserInfoById(userID) { success, response ->
                if (success) {
                    // 网络获取成功后缓存用户信息
                    response?.let {
                        val userCache = UserCache(it.userId, it.userName, it.userAvatar, it.createtime, it.userGoodsOnSaleCount, it.userContactCount)
                        viewModelScope.launch {
                            databaseRepository.saveUserCache(userCache)
                        }
                    }
                    userBaseInfo.postValue(response)
                } else {
                    userBaseInfo.postValue(null)
                }
            }
        }
        return userBaseInfo
    }
}
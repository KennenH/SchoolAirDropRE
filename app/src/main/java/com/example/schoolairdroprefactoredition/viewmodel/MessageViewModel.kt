package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MessageViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    private val userCacheLiveData = MutableLiveData<UserCache>()

    private val userRepository by lazy {
        UserRepository.getInstance()
    }

    class MessageViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
                return MessageViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 从本地获取消息列表
     *
     * 是本地未读和网络离线在本地混合之后的
     */
    fun getChatList(receiverID: String): LiveData<List<ChatOfflineNumDetail>> {
        return databaseRepository.getChatList(receiverID).asLiveData()
    }

    /**
     * 侧滑删除会话，即将会话的display置为0
     * 同时会将该用户的本地未读消息ack
     *
     * 何时置1详见[com.example.schoolairdroprefactoredition.database.dao.DatabaseDao]
     */
    fun swipeToHideChannel(myID: String, counterpartID: String) {
        viewModelScope.launch {
            databaseRepository.hideChannel(myID, counterpartID)
        }
    }

    /**
     * 获取用户缓存，当有缓存存在直接使用缓存
     */
    fun getUserCache(userId: Int): LiveData<UserCache?> {
        viewModelScope.launch {
            databaseRepository.getUserCache(userId)
                    .let { cache ->
                        if (cache?.user_avatar != null && cache.user_name != null) {
                            // 若有缓存直接使用缓存，无需获取网络信息
                            userCacheLiveData.value = cache
                        } else {
                            // 若无缓存便去获取一次即可，进入个人主页才去请求更新用户信息
                            userRepository.getUserInfoById(userId) { success, response ->
                                if (success) {
                                    // 网络获取成功则返回并保存
                                    response?.let {
                                        val userCache = UserCache(it.userId, it.userName, it.userAvatar, it.createtime, it.userGoodsOnSaleCount, it.userContactCount)
                                        userCacheLiveData.postValue(userCache)
                                        viewModelScope.launch {
                                            databaseRepository.saveUserCache(userCache)
                                        }
                                    }
                                } else {
                                    userCacheLiveData.postValue(null)
                                }
                            }
                        }
                    }
        }
        return userCacheLiveData
    }
}
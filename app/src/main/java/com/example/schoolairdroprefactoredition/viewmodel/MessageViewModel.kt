package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.domain.DomainOfflineNum
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UserRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList

class MessageViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    private var userCacheLiveData = MutableLiveData<UserCache>()

    private var offlineNumLiveData = MutableLiveData<DomainOfflineNum>()

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
     * 获取网络离线消息数量
     *
     * 不能直接显示，要和本地消息列表混合之后再查询
     */
    fun getOfflineNumOnline(token: DomainToken?): LiveData<DomainOfflineNum?> {
        if (token != null) {
            viewModelScope.launch {
                databaseRepository.getOfflineNum(token) { success, response ->
                    if (success) {
                        offlineNumLiveData.postValue(response)
                    } else {
                        offlineNumLiveData.postValue(null)
                    }
                }
            }
        } else {
            offlineNumLiveData.postValue(null)
        }
        return offlineNumLiveData
    }

    /**
     * 保存服务器离线消息数量以及最新10条消息，合并入本地数据库
     */
    fun saveOfflineNum(offlineNums: DomainOfflineNum) {
        viewModelScope.launch {
            // 消息数量数组
            val numList: ArrayList<ChatOfflineNum> = ArrayList()
            // 消息记录数组
            val historyList: ArrayList<ChatHistory> = ArrayList()
            // 最后一条消息数组
            val lastFromUserInformation: ArrayList<LastFromUserInformation> = ArrayList()
            // 用户基本信息
            val senderInfo: ArrayList<UserCache> = ArrayList()

            val data = offlineNums.data
            for (offlineNum in data) {
                // 装配消息数量
                numList.add(ChatOfflineNum(
                        offlineNum.senderId,
                        offlineNum.receiverId,
                        offlineNum.offlineNum,
                        offlineNum.fingerPrint,
                        1))

                // 装配消息记录
                for (offlineBean in offlineNum.offline) {
                    historyList.add(ChatHistory(
                            offlineBean.fingerPrint,
                            offlineNum.senderId,
                            offlineNum.receiverId,
                            offlineBean.messageType,
                            offlineBean.message,
                            offlineBean.sendTime,
                            1))
                }

                // 装配最后一条消息
                lastFromUserInformation.add(LastFromUserInformation(
                        offlineNum.senderId,
                        offlineNum.fingerPrint,
                        offlineNum.offline.size == ConstantUtil.DATA_FETCH_DEFAULT_SIZE
                ))

                // 装配用户信息
                senderInfo.add(UserCache(offlineNum.senderId.toInt(), offlineNum.senderInfo.senderName, offlineNum.senderInfo.senderAvatar, null, null, null))
            }

            // 保存所有装配好的信息
            databaseRepository.saveLastMessage(lastFromUserInformation)
            databaseRepository.saveOfflineNum(numList)
            databaseRepository.saveHistory(historyList)
            databaseRepository.saveUserCache(senderInfo)
        }
    }

    /**
     * 侧滑删除会话，即将会话的display置为0
     *
     * 何时置1详见[com.example.schoolairdroprefactoredition.database.dao.ChatHistoryDao]
     */
    fun swipeToHideChannel(counterpartId: String) {
        viewModelScope.launch {
            databaseRepository.hideChannel(counterpartId)
        }
    }

    /**
     * 获取用户基本信息
     */
    fun getUserBaseInfo(userId: Int): LiveData<UserCache?> {
        viewModelScope.launch {
            databaseRepository.getUserCache(userId)
                    .let { cache ->
                        if (cache != null) {
                            // 若有缓存直接使用缓存，无需获取网络信息
                            userCacheLiveData.value = cache
                        } else {
                            // 若无缓存才去获取
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
package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.domain.DomainOfflineNum
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.ChatAllRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MessageUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList

class MessageViewModel(private val chatRepository: ChatAllRepository) : ViewModel() {

    private var chatHistoryLiveData = MutableLiveData<List<ChatHistory>>()

    private var userCacheLiveData = MutableLiveData<UserCache>()

    private var offlineNumLiveData = MutableLiveData<DomainOfflineNum>()

    var offlineNumLoadState = MutableLiveData<LoadState>()

    class MessageViewModelFactory(private val repository: ChatAllRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
                return MessageViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 获取消息列表
     * 是本地未读和离线未读合并的
     */
    fun getChatList(receiverID: String): LiveData<List<ChatOfflineNumDetail>> {
        return chatRepository.getChatList(receiverID).asLiveData()
    }

    fun getOfflineNumOnline(token: DomainToken): LiveData<DomainOfflineNum> {
        offlineNumLoadState.value = LoadState.LOADING
        viewModelScope.launch {
            chatRepository.getOfflineNum(token) { success, response ->
                if (success) {
                    offlineNumLoadState.value = LoadState.SUCCESS
                    offlineNumLiveData.value = response
                } else {
                    offlineNumLoadState.value = LoadState.ERROR
                }
            }
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

            val data = offlineNums.data
            for (offlineNum in data) {
                lastFromUserInformation.add(LastFromUserInformation(
                        offlineNum.senderId,
                        offlineNum.fingerPrint,
                        offlineNum.offline.size == ConstantUtil.DATA_FETCH_DEFAULT_SIZE
                ))

                // 消息数量
                numList.add(ChatOfflineNum(
                        offlineNum.senderId,
                        offlineNum.senderInfo.senderName,
                        offlineNum.senderInfo.senderAvatar,
                        offlineNum.receiverId,
                        offlineNum.offlineNum,
                        offlineNum.fingerPrint,
                        1))

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
            }

            chatRepository.saveLatestMessage(lastFromUserInformation)
            chatRepository.saveOfflineNum(numList)
            chatRepository.saveHistory(historyList)
        }
    }

    /**
     * 保存消息记录
     */
    fun saveHistories(histories: List<ChatHistory>) {
        viewModelScope.launch {
            chatRepository.saveHistory(histories)
        }
    }

    /**
     * ack消息数量
     */
    fun ackOfflineNum(receiverID: String, senderID: String) =
            viewModelScope.launch {
                chatRepository.ackOfflineNum(receiverID, senderID)
            }
}
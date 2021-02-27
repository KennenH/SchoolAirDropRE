package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.database.pojo.ChatOfflineNum
import com.example.schoolairdroprefactoredition.database.pojo.PullFlag
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainOfflineNum
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InstanceMessageViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    private val hasOffline = MutableLiveData<Boolean>()

    class InstanceViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InstanceMessageViewModel::class.java)) {
                return InstanceMessageViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 保存接收到的消息
     *
     * repository层会将该会话display置1
     */
    fun saveReceivedMessage(history: ChatHistory) {
        viewModelScope.launch {
            databaseRepository.saveHistory(history, true)
        }
    }

    /**
     * 获取网络离线消息数量
     *
     * 不能直接显示，要和本地消息列表混合之后再查询
     * @return 是否有离线消息
     */
    fun getOfflineNumOnline(token: DomainToken?, listener: MainActivity.OnOfflineNumStateChangeListener?): LiveData<Boolean> {
        if (token != null) {
            listener?.onPullingOfflineNum()
            viewModelScope.launch {
                databaseRepository.getOfflineNum(token) { response ->
                    listener?.onPullDone()
                    response?.let {
                        hasOffline.value = it.data.isNotEmpty()
                        saveOfflineNum(it)
                    }
                }
            }
        }
        return hasOffline
    }

    /**
     * 保存服务器离线消息数量以及最新10条消息，合并入本地数据库
     */
    private fun saveOfflineNum(offlineNums: DomainOfflineNum) {
        viewModelScope.launch {
            // 消息数量数组
            val numList: ArrayList<ChatOfflineNum> = ArrayList()
            // 消息记录数组
            val historyList: ArrayList<ChatHistory> = ArrayList()
            // 最后一条消息数组
            val pullFlag: ArrayList<PullFlag> = ArrayList()
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
                            ChatRecyclerAdapter.MessageSendStatus.SUCCESS))
                }

                // 装配最后一条消息
                pullFlag.add(PullFlag(
                        offlineNum.senderId,
                        offlineNum.offline.isNotEmpty()))

                // 装配用户信息
                senderInfo.add(UserCache(offlineNum.senderId.toInt(), offlineNum.senderInfo.senderName, offlineNum.senderInfo.senderAvatar, null, null, null))
            }

            // 保存所有装配好的信息
            databaseRepository.saveUserCache(senderInfo)
            databaseRepository.saveLastMessage(pullFlag)
            databaseRepository.saveOfflineNum(numList)
            databaseRepository.saveHistory(historyList)
        }
    }

    /**
     * 更新未被收到的消息数组状态
     */
    fun messagesLost(fingerprints: List<String>) {
        viewModelScope.launch {
            for (fingerprint in fingerprints) {
                databaseRepository.updateMessageStatus(fingerprint, ChatRecyclerAdapter.MessageSendStatus.FAILED)
            }
        }
    }

    /**
     * 更新已被收到的消息状态
     */
    fun messageBeReceived(fingerprint: String) {
        viewModelScope.launch {
            databaseRepository.updateMessageStatus(fingerprint, ChatRecyclerAdapter.MessageSendStatus.SUCCESS)
        }
    }
}
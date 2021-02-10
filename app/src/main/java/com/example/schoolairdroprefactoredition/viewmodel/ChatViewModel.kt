package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.database.pojo.LastFromUserInformation
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.repository.ChatAllRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MessageUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * 聊天页面使用的viewmodel
 * 用以保存从服务器二次拉取的离线消息
 * 首次拉取在MessageFragment被初始化的时候便
 */
class ChatViewModel(private val chatLocalRepository: ChatAllRepository) : ViewModel() {

    private val ackList = ArrayList<String>()

    class ChatViewModelFactory(private val repository: ChatAllRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 历史消息
     */
    private var chatLiveData = MutableLiveData<List<ChatHistory>>()

    /**
     * 保存自己发送的消息
     * 该消息刚刚被发送，成功与否还未知
     */
    fun saveSentMessage(history: ChatHistory) {
        viewModelScope.launch {
            chatLocalRepository.saveHistory(history)
        }
    }

    /**
     * 保存从服务器获取的二次拉取的离线消息
     */
    private fun saveReceivedOffline(offline: List<DomainOffline.DataBean>) {
        viewModelScope.launch {
            chatLocalRepository.saveHistory(MessageUtil.offlineToChatHistory(offline))
        }
    }

    /**
     * 获取与某用户的历史记录
     * 方法流程：
     * 先找到来自这个用户的[LastFromUserInformation]
     * 然后去获取本地数据并显示
     * 此时判断pullFlag，若为true则从服务器上预拉取数据并保存至本地
     * 然后保存离线消息数据和更新[LastFromUserInformation]的信息
     */
    fun getChat(token: String, receiverID: String, senderID: String): LiveData<List<ChatHistory>> {
        viewModelScope.launch {
            // 获取用户最后一条消息的指纹和是否还有来自这个用户的消息的标志
            val lastMessage = chatLocalRepository.getLastFromUserInformation(senderID)
            val fingerprint = lastMessage?.fingerprint
            // 获取本地数据
            chatLiveData.postValue(chatLocalRepository.getChatLocal(receiverID, senderID, fingerprint))
            // 若上一次保存的flag是true则预拉取服务器数据
            if (lastMessage != null && lastMessage.pull_flag && fingerprint != null) {
                chatLocalRepository.getChatRemote(token, senderID, fingerprint, ackList) { success, response ->
                    if (success && response != null) {
                        val data = response.data
                        // 保存获取到的数据
                        saveReceivedOffline(data)
                        val lastFingerprint = data.last().fingerPrint
                        // 保存最新消息指纹
                        viewModelScope.launch {
                            chatLocalRepository.saveLatestMessage(LastFromUserInformation(senderID, lastFingerprint, data.size == ConstantUtil.DATA_FETCH_DEFAULT_SIZE))
                        }
                    }
                }
            }
        }
        return chatLiveData
    }
}
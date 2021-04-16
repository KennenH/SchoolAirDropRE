package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.database.pojo.PullFlag
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MessageUtil
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 * 聊天页面使用的view model
 * 用以保存从服务器二次拉取的离线消息
 * 首次拉取在MessageFragment被初始化的时候便
 */
class ChatViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    private val uploadRepository by lazy {
        UploadRepository.getInstance()
    }

    class ChatViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 历史消息live data
     */
    private val chatLiveData = MutableLiveData<List<ChatHistory>>()

    /**
     * 更新消息发送状态
     */
    fun updateMessageStatus(fingerprint: String, @ChatRecyclerAdapter.MessageSendStatus status: Int) {
        viewModelScope.launch {
            databaseRepository.updateMessageStatus(fingerprint, status)
        }
    }

    /**
     * 上传聊天的图片
     *
     * 图片上传成功之后服务器将会返回图片的路径，再将消息的typeu置为1，消息内容置为获取到的图片路径
     *
     * @param imagePaths 本地图片路径
     */
    fun sendImageMessage(token: String?, imagePaths: List<String>): LiveData<List<String>?> {
        val uploadLiveDate = MutableLiveData<List<String>?>()
        if (token != null) {
            uploadRepository.upload(
                    token,
                    imagePaths,
                    ConstantUtil.UPLOAD_TYPE_IM) { _, _, taskAndKeys, _ ->
                if (taskAndKeys != null) {
                    uploadRepository.moveIMImage(token, taskAndKeys.taskId, taskAndKeys.keys.joinToString(",")) {
                        if (it != null) {
                            uploadLiveDate.postValue(it.data.path_url)
                        } else {
                            uploadLiveDate.postValue(null)
                        }
                    }
                } else {
                    uploadLiveDate.postValue(null)
                }
            }
        } else {
            uploadLiveDate.postValue(null)
        }
        return uploadLiveDate
    }

    /**
     * 保存自己发送的消息
     *
     * repository层会将改会话display置1
     */
    fun saveSentMessage(history: ChatHistory) {
        viewModelScope.launch {
            databaseRepository.saveHistory(history, false)
        }
    }

    /**
     * ack会话消息数量
     */
    fun ackOfflineNum(receiverID: String, senderID: String) {
        viewModelScope.launch {
            databaseRepository.ackOfflineNum(receiverID, senderID)
        }
    }

    /**
     * 保存从服务器获取的二次拉取的离线消息
     */
    private fun saveReceivedOffline(offline: List<DomainOffline.DataBean>) {
        viewModelScope.launch {
            databaseRepository.saveHistory(MessageUtil.offlineToChatHistory(offline))
        }
    }

    /**
     * 获取与某用户的历史记录
     * 方法流程：
     * 先找到来自这个用户的[PullFlag]
     * 然后去获取本地数据并显示
     * 此时判断pullFlag，若为true则从服务器上预拉取数据并保存至本地
     * 然后保存离线消息数据和更新[PullFlag]的信息
     *
     * @param startTime 从该临界时间开始检索更早地消息
     */
    fun getChat(token: String?, receiverID: String, senderID: String, startTime: Long? = null): LiveData<List<ChatHistory>> {
        viewModelScope.launch {
            // 获取本地聊天记录
            chatLiveData.postValue(databaseRepository.getChatLocal(receiverID, senderID, startTime))

            // 获取服务器离线并ack上一批显示的消息
            if (token != null) {
                val pullFlag = databaseRepository.getPullFlag(senderID)
                // 若上一次保存的flag是true则预拉取服务器数据并ack上一批获取的离线消息
                if (pullFlag != null && pullFlag.pull_flag) {
                    databaseRepository.getChatRemote(token, senderID, startTime
                            ?: System.currentTimeMillis()) { response ->
                        if (response != null) {
                            val data = response.data
                            // 保存获取到的数据
                            saveReceivedOffline(data)

                            // 本次预拉取的消息数量不足默认数量，则直接将它们ack
                            val size = data.size
                            if (size > 0 && size < ConstantUtil.DEFAULT_PAGE_SIZE) {
                                databaseRepository.ackOffline(token, senderID, data.last().send_time)
                            }

                            // 小于默认条数就将标志为置为
                            viewModelScope.launch {
                                databaseRepository.savePullFlag(
                                        // 只要获取到的离线消息数量不足默认数就需要再次拉取
                                        PullFlag(senderID, size < ConstantUtil.DEFAULT_PAGE_SIZE))
                            }
                        }
                    }
                }
            }
            joinAll()
        }
        return chatLiveData
    }
}
package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
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
import java.lang.IllegalArgumentException

/**
 * 聊天页面使用的view model
 * 用以保存从服务器二次拉取的离线消息
 * 首次拉取在MessageFragment被初始化的时候便
 */
class ChatViewModel(private val databaseRepository: DatabaseRepository, application: Application) : AndroidViewModel(application) {

    private val uploadRepository by lazy {
        UploadRepository.getInstance()
    }

    class ChatViewModelFactory(private val repository: DatabaseRepository, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(repository, application) as T
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
    fun updateMessageStatus(fingerprint: String,@ChatRecyclerAdapter.MessageSendStatus status: Int) {
        viewModelScope.launch {
            databaseRepository.updateMessageStatus(fingerprint, status)
        }
    }

    /**
     * 上传聊天的图片
     *
     * 图片上传成功之后服务器将会返回图片的路径，再将消息的typeu置为1，消息内容置为获取到的图片路径
     */
    fun sendImageMessage(token: String?, imagePaths: List<String>): LiveData<List<String>?> {
        val uploadLiveDate = MutableLiveData<List<String>?>()
        if (token != null) {
            uploadRepository.upload(token, imagePaths, ConstantUtil.UPLOAD_TYPE_IM) {
                if (it != null) {
                    uploadRepository.moveIMImage(token, it.taskId, it.keys.joinToString(",")) {
                        uploadLiveDate.postValue(null)
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
            // 2021/2/23  Bug fix： 以获取的离线数量小于默认值设置flag来判断是否需要请求会有问题，最后一次
            // 请求必定小于默认值，这部分离线消息将永远无法被ack
            //            update ： 将flag设置为false的逻辑改为只有当获取到的离线数量为0时才不再请求
            if (token != null) {
                val pullFlag = databaseRepository.getPullFlag(senderID)
                // 若上一次保存的flag是true则预拉取服务器数据并ack上一批获取的离线消息
                if (pullFlag != null && pullFlag.pull_flag) {
                    databaseRepository.getChatRemote(token, senderID, startTime
                            ?: System.currentTimeMillis()) { success, response ->
                        if (success && response != null) {
                            val data = response.data
                            // 保存获取到的数据
                            saveReceivedOffline(data)
                            // 保存这一批消息中最早的消息的指纹
                            viewModelScope.launch {
                                databaseRepository.saveLastMessage(
                                        // 只要获取到的离线不是0就为true
                                        PullFlag(senderID, data.isNotEmpty()))
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
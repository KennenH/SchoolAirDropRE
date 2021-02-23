package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.blankj.utilcode.util.TimeUtils
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.database.pojo.LastFromUserInformation
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.repository.UserRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MessageUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

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
     * 聊天图片的live data
     */
    private val uploadLiveDate = MutableLiveData<List<String>?>()

    /**
     * 上传聊天的图片
     *
     * 图片上传成功之后服务器将会返回图片的路径，再将消息的typeu置为1，消息内容置为获取到的图片路径
     */
    fun sendImageMessage(token: String?, imagePaths: List<String>): LiveData<List<String>?> {
        if (token != null) {
            uploadRepository.upload(token, imagePaths, ConstantUtil.UPLOAD_TYPE_IM) {
                if (it != null) {
                    // todo
//                    uploadLiveDate.postValue(null)
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
     * 先找到来自这个用户的[LastFromUserInformation]
     * 然后去获取本地数据并显示
     * 此时判断pullFlag，若为true则从服务器上预拉取数据并保存至本地
     * 然后保存离线消息数据和更新[LastFromUserInformation]的信息
     *
     * @param startTime 从该临界时间开始检索
     */
    fun getChat(token: String?, receiverID: String, senderID: String, startTime: Long? = null): LiveData<List<ChatHistory>> {
        viewModelScope.launch {
            // 获取本地聊天记录
            chatLiveData.postValue(databaseRepository.getChatLocal(receiverID, senderID, startTime))

            // 获取服务器离线
            if (token != null) {
                // 获取来自该用户上一批获取的最早消息的时间和是否还有来自这个用户的消息的标志
                val earliestPulledMessageFromThisUser = databaseRepository.getLastFromUserInformation(senderID)
                // 若上一次保存的flag是true则预拉取服务器数据，若离线消息数量拉取时已经小于默认值则无需对于该用户额外拉取
                if (earliestPulledMessageFromThisUser != null && earliestPulledMessageFromThisUser.pull_flag) {
                    databaseRepository.getChatRemote(token, senderID, startTime
                            ?: System.currentTimeMillis()) { success, response ->
                        if (success && response != null) {
                            val data = response.data
                            // 保存获取到的数据
                            saveReceivedOffline(data)
                            // 保存这一批消息中最早的消息的指纹
                            viewModelScope.launch {
                                databaseRepository.saveLastMessage(
                                        LastFromUserInformation(senderID, data.size >= ConstantUtil.DATA_FETCH_DEFAULT_SIZE))
                            }
                        }
                    }
                }
            }
        }
        return chatLiveData
    }
}
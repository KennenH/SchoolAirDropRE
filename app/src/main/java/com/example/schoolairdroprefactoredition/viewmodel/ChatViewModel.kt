package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.database.pojo.LastFromUserInformation
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MessageUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * 聊天页面使用的view model
 * 用以保存从服务器二次拉取的离线消息
 * 首次拉取在MessageFragment被初始化的时候便
 */
class ChatViewModel(private val chatLocalRepository: DatabaseRepository, application: Application) : AndroidViewModel(application) {

    private val uploadRepository by lazy {
        UploadRepository.getInstance()
    }

    /**
     * 下一批将要被ack的消息组
     *
     * todo 如何解决已被ack的消息多次去ack浪费服务器资源，要么本地存一个是否ack的表，自己发送和在线收到的默认已经ack
     */
    private val ackList = ArrayList<String>()

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
    private val uploadLiveDate = MutableLiveData<String?>()

    /**
     * 上传聊天的图片
     *
     * 图片上传成功之后服务器将会返回图片的路径，再将消息的typeu置为1，消息内容置为获取到的图片路径
     */
    fun uploadImage(imagePaths: List<String>): LiveData<String?> {
        uploadRepository.uploadImage(
                getApplication(),
                imagePaths,
                ConstantUtil.UPLOAD_TYPE_IM) { response ->
            uploadLiveDate.postValue(response?.data?.images)
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
            chatLocalRepository.saveHistory(history, false)
        }
    }

    /**
     * ack会话消息数量
     */
    fun ackOfflineNum(receiverID: String, senderID: String) {
        viewModelScope.launch {
            chatLocalRepository.ackOfflineNum(receiverID, senderID)
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
    fun getChat(token: String?, receiverID: String, senderID: String, start: String? = null): LiveData<List<ChatHistory>> {
        viewModelScope.launch {
            // 获取用户最早消息的指纹和是否还有来自这个用户的消息的标志
//            val lastMessage = chatLocalRepository.getLastFromUserInformation(senderID)
//            val fingerprint = lastMessage?.fingerprint
            // 获取本地默认数量条数据
            chatLiveData.postValue(chatLocalRepository.getChatLocal(receiverID, senderID, start))
            // 若上一次保存的flag是true则预拉取服务器数据，若离线消息数量拉取时便小于默认值则无需对于该
            // 用户额外拉取
//            if (lastMessage != null && lastMessage.pull_flag && fingerprint != null) {
//                chatLocalRepository.getChatRemote(token, senderID, fingerprint, ackList) { success, response ->
//                    if (success && response != null) {
//                        val data = response.data
//                        // 保存获取到的数据
//                        saveReceivedOffline(data)
//                        // 保存这一批消息中最早的消息的指纹
//                        viewModelScope.launch {
//                            chatLocalRepository.saveLastMessage(
//                                    LastFromUserInformation(senderID, data.first().fingerPrint, data.size >= ConstantUtil.DATA_FETCH_DEFAULT_SIZE))
//                        }
//                    }
//                }
//            }
        }
        return chatLiveData
    }
}
package com.example.schoolairdroprefactoredition.repository

import androidx.annotation.WorkerThread
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.database.dao.ChatHistoryDao
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.domain.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class DatabaseRepository(private val chatHistoryDao: ChatHistoryDao) {

    fun getChatList(receiverID: String): Flow<List<ChatOfflineNumDetail>> {
        return chatHistoryDao.getChatList(receiverID)
    }

    /**
     * 获取本地数据库数据
     */
    suspend fun getChatLocal(receiverID: String, senderID: String, start: String?): List<ChatHistory> {
        return if (start == null) {
            chatHistoryDao.getLatestChat(receiverID, senderID)
        } else {
            chatHistoryDao.getChat(receiverID, senderID, start)
        }
    }

    /**
     * 获取服务器消息
     */
    fun getChatRemote(token: String, senderID: String, fingerprint: String, ackList: List<String>? = null, onResult: (success: Boolean, response: DomainOffline?) -> Unit) {
        RetrofitClient.imApi.getOffline(token, senderID, fingerprint, ackList).apply {
            enqueue(object : CallBackWithRetry<DomainOffline>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainOffline>, response: Response<DomainOffline>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.isSuccess) {
                            onResult(true, body)
                        } else {
                            onResult(false, null)
                        }
                    } else {
                        onResult(false, null)
                    }
                }
            })
        }
    }

    fun getOfflineNum(token: DomainToken, onResult: (success: Boolean, response: DomainOfflineNum?) -> Unit) {
        RetrofitClient.imApi.getOfflineNum(token.access_token).apply {
            enqueue(object : CallBackWithRetry<DomainOfflineNum>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainOfflineNum>, response: Response<DomainOfflineNum>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null) {
                            if (body.isSuccess) {
                                onResult(true, body)
                            } else {
                                onResult(false, null)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }
                }
            })
        }
    }

    @WorkerThread
    suspend fun hideChannel(counterpartId: String) {
        chatHistoryDao.setDisplay(counterpartId, 0)
    }

    @WorkerThread
    suspend fun saveLastMessage(lastFromUserInformation: LastFromUserInformation) {
        chatHistoryDao.saveLastMessage(lastFromUserInformation)
    }

    @WorkerThread
    suspend fun saveLastMessage(lastFromUserInformation: List<LastFromUserInformation>) {
        chatHistoryDao.saveLastMessage(lastFromUserInformation)
    }

    @WorkerThread
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>) {
        chatHistoryDao.saveOfflineNum(offlineNums)
    }

    @WorkerThread
    suspend fun saveUserCache(userCaches: List<UserCache>) {
        chatHistoryDao.saveUserCache(userCaches)
    }

    @WorkerThread
    suspend fun saveUserCache(userCache: UserCache) {
        chatHistoryDao.saveUserCache(userCache)
    }

    @WorkerThread
    suspend fun getUserCache(userID: Int): UserCache? {
        return chatHistoryDao.getUserCache(userID)
    }

    /**
     * @param isSentFromCounterpart
     * 是否是对方发来的消息，即该消息是否是我接收到的
     */
    @WorkerThread
    suspend fun saveHistory(history: ChatHistory, isSentFromCounterpart: Boolean) {
        // 保存消息本身
        chatHistoryDao.saveChat(history)
        // 更新消息列表
        if (isSentFromCounterpart) {
            chatHistoryDao.saveOfflineNum(ChatOfflineNum(history.sender_id, history.receiver_id, 1, history.fingerprint, 1), true)
        } else {
            chatHistoryDao.saveOfflineNum(ChatOfflineNum(history.receiver_id, history.sender_id, 1, history.fingerprint, 1), false)
        }
    }

    @WorkerThread
    suspend fun saveHistory(histories: List<ChatHistory>) {
        chatHistoryDao.saveChat(histories)
    }

    @WorkerThread
    suspend fun ackOfflineNum(receiverID: String, senderID: String) {
        chatHistoryDao.ackOfflineNum(receiverID, senderID)
    }

    @WorkerThread
    suspend fun getLastFromUserInformation(senderID: String): LastFromUserInformation? {
        return chatHistoryDao.getLastFromUserInformation(senderID)
    }

}
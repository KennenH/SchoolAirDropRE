package com.example.schoolairdroprefactoredition.repository

import androidx.annotation.WorkerThread
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.database.dao.ChatHistoryDao
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.domain.*
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
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
     * 获取本地数据库消息
     */
    suspend fun getChatLocal(receiverID: String, senderID: String, start: Long?): List<ChatHistory> {
        return if (start == null) {
            chatHistoryDao.getLatestChat(receiverID, senderID)
        } else {
            chatHistoryDao.getChat(receiverID, senderID, start)
        }
    }

    /**
     * 获取某个临界时间之前的（早的，旧的）消息
     */
    fun getChatRemote(token: String, senderID: String, start: Long, onResult: (success: Boolean, response: DomainOffline?) -> Unit) {
        RetrofitClient.imApi.getOffline(token, senderID, start).apply {
            enqueue(object : CallBackWithRetry<DomainOffline>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainOffline>, response: Response<DomainOffline>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.code == ConstantUtil.HTTP_OK) {
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

    /**
     * 获取离线消息数量，即离线消息列表
     */
    fun getOfflineNum(token: DomainToken, onResult: (success: Boolean, response: DomainOfflineNum?) -> Unit) {
        RetrofitClient.imApi.getOfflineNum(token.access_token).apply {
            enqueue(object : CallBackWithRetry<DomainOfflineNum>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainOfflineNum>, response: Response<DomainOfflineNum>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.code == ConstantUtil.HTTP_OK) {
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

    @WorkerThread
    suspend fun hideChannel(counterpartId: String) {
        chatHistoryDao.setDisplay(counterpartId, 0)
    }

    @WorkerThread
    suspend fun saveLastMessage(pullFlag: PullFlag) {
        chatHistoryDao.updatePullFlag(pullFlag)
    }

    @WorkerThread
    suspend fun saveLastMessage(pullFlag: List<PullFlag>) {
        chatHistoryDao.updatePullFlag(pullFlag)
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
    suspend fun getUserCache(userID: Int):  UserCache? {
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
            // !!重要!!  必须先保存用户信息，即使只有id也要保存，详见方法本身
            chatHistoryDao.saveFirstUserCache(UserCache(history.sender_id.toInt(), null, null, null, null, null))

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
    suspend fun getPullFlag(senderID: String): PullFlag? {
        return chatHistoryDao.getPullFlag(senderID)
    }

}
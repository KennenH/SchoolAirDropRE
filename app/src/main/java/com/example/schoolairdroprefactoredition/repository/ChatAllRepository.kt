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

class ChatAllRepository(private val chatHistoryDao: ChatHistoryDao) {

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

    /**
     * 获取本地用户信息缓存
     */
    fun getUserInfoCache(userID: Int): UserCache? {
        val userInfo = chatHistoryDao.getUserInfo(userID)
        return if (userInfo.isNotEmpty()) {
            userInfo[0]
        } else {
            null
        }
    }

    /**
     * 获取服务器用户信息
     */
    fun getUserInfoOnline(userID: Int, onResult: (success: Boolean, response: DomainUserInfo.DataBean?) -> Unit) {
        RetrofitClient.userApi.getUserInfoByID(userID).apply {
            enqueue(object : CallBackWithRetry<DomainUserInfo>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(false, null)
                }

                override fun onResponse(call: Call<DomainUserInfo>, response: Response<DomainUserInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            onResult(true, body?.data)
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
    suspend fun saveLatestMessage(lastFromUserInformation: LastFromUserInformation) {
        chatHistoryDao.saveLastMessage(lastFromUserInformation)
    }

    @WorkerThread
    suspend fun saveLatestMessage(lastFromUserInformation: List<LastFromUserInformation>) {
        chatHistoryDao.saveLastMessage(lastFromUserInformation)
    }

    @WorkerThread
    suspend fun saveUserCache(userCache: UserCache) {
        chatHistoryDao.saveUserInfo(userCache)
    }

    @WorkerThread
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>) {
        chatHistoryDao.saveOfflineNum(offlineNums)
    }

    @WorkerThread
    suspend fun saveHistory(histories: List<ChatHistory>) {
        chatHistoryDao.saveChat(histories)
    }

    @WorkerThread
    suspend fun saveHistory(history: ChatHistory) {
        chatHistoryDao.saveChat(history)
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
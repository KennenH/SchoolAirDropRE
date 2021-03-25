package com.example.schoolairdroprefactoredition.repository

import androidx.annotation.WorkerThread
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.database.dao.DatabaseDao
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.domain.*
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheConstantUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class DatabaseRepository(private val databaseDao: DatabaseDao) {

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 获取消息列表
     */
    fun getChatList(receiverID: String): Flow<List<ChatOfflineNumDetail>> {
        return databaseDao.getChatList(receiverID)
    }

    /**
     * 获取本地数据库消息
     */
    suspend fun getChatLocal(receiverID: String, senderID: String, start: Long?): List<ChatHistory> {
        return if (start == null) {
            databaseDao.getLatestChat(receiverID, senderID)
        } else {
            databaseDao.getChat(receiverID, senderID, start)
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
    fun getOfflineNum(token: DomainToken, onResult: (response: DomainOfflineNum?) -> Unit) {
        RetrofitClient.imApi.getOfflineNum(token.access_token).apply {
            enqueue(object : CallBackWithRetry<DomainOfflineNum>(this@apply) {
                override fun onFailureAllRetries() {
                    onResult(null)
                }

                override fun onResponse(call: Call<DomainOfflineNum>, response: Response<DomainOfflineNum>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.code == ConstantUtil.HTTP_OK) {
                            onResult(body)
                        } else {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                }
            })
        }
    }

    /**
     * ack消息，从start开始，比start发送时间迟的消息都将被ack
     */
    fun ackOffline(token: String, senderID: String, start: Long) {
        RetrofitClient.imApi.ackOffline(token, senderID, start)
    }

    /**
     * 隐藏域某人的会话
     */
    @WorkerThread
    suspend fun hideChannel(myID: String, counterpartId: String) {
        databaseDao.setChannelDisplay(myID, counterpartId, 0)
        databaseDao.ackOfflineNum(counterpartId, myID)
    }

    /**
     * 保存是否需要拉取消息的标识符
     */
    @WorkerThread
    suspend fun savePullFlag(pullFlag: PullFlag) {
        databaseDao.updatePullFlag(pullFlag)
    }

    /**
     * 批量保存是否需要拉取消息的标识符
     */
    @WorkerThread
    suspend fun savePullFlag(pullFlag: List<PullFlag>) {
        databaseDao.updatePullFlag(pullFlag)
    }

    /**
     * 保存离线消息数量
     */
    @WorkerThread
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>) {
        databaseDao.saveOfflineNum(offlineNums)
    }

    /**
     * 批量保存用户信息缓存
     */
    @WorkerThread
    suspend fun saveUserCache(userCaches: List<UserCache>) {
        databaseDao.saveUserCache(userCaches)
    }

    /**
     * 保存用户信息缓存
     */
    @WorkerThread
    suspend fun saveUserCache(userCache: UserCache) {
        // 保存再次请求的限制，防止频繁调用请求
        jsonCacheUtil.saveCache(JsonCacheConstantUtil.IS_GET_USER_INFO_PRESENTLY + userCache.user_id, true, JsonCacheConstantUtil.NEXT_GET_TIME_SPAN)
        databaseDao.saveUserCache(userCache)
    }

    /**
     * 获取用户信息缓存
     */
    @WorkerThread
    suspend fun getUserCache(userID: Int): UserCache? {
        return databaseDao.getUserCache(userID)
    }

    /**
     * 保存消息记录
     *
     * @param isSentFromCounterpart
     * 是否是对方发来的消息，即该消息是否是我接收到的
     */
    @WorkerThread
    suspend fun saveHistory(history: ChatHistory, isSentFromCounterpart: Boolean) {
        // 保存消息本身
        databaseDao.saveChat(history)
        // 更新消息列表
        if (isSentFromCounterpart) {
            databaseDao.saveOfflineNum(ChatOfflineNum(history.sender_id, history.receiver_id, 1, history.fingerprint, 1), true)
        } else {
            databaseDao.saveOfflineNum(ChatOfflineNum(history.receiver_id, history.sender_id, 1, history.fingerprint, 1), false)
        }
    }

    /**
     * 批量保存消息记录
     */
    @WorkerThread
    suspend fun saveHistory(histories: List<ChatHistory>) {
        databaseDao.saveChat(histories)
    }

    /**
     * ack消息数量
     */
    @WorkerThread
    suspend fun ackOfflineNum(receiverID: String, senderID: String) {
        databaseDao.ackOfflineNum(receiverID, senderID)
    }

    /**
     * 获取对于该用户是否还需要拉取离线消息
     */
    @WorkerThread
    suspend fun getPullFlag(senderID: String): PullFlag? {
        return databaseDao.getPullFlag(senderID)
    }

    /**
     * 收藏物品
     */
    @WorkerThread
    suspend fun addFavorite(favorite: Favorite) {
        databaseDao.addFavorite(favorite)
    }

    /**
     * 取消收藏物品
     */
    @WorkerThread
    suspend fun removeFavorite(goodsID: Int) {
        databaseDao.removeFavorite(goodsID)
    }

    /**
     * 检查物品是否被收藏了
     */
    @WorkerThread
    suspend fun isFavorite(goodsID: Int): Boolean {
        return databaseDao.isFavorite(goodsID)
    }

    /**
     * 更新消息发送状态
     */
    @WorkerThread
    suspend fun updateMessageStatus(fingerprint: String, @ChatRecyclerAdapter.MessageSendStatus status: Int) {
        databaseDao.updateMessageStatus(fingerprint, status)
    }

    /**
     * 获取所有收藏的物品
     */
    @WorkerThread
    suspend fun getFavorites(key: String?): List<Favorite> {
        return if (key == null) databaseDao.getFavorites()
        else databaseDao.getFavorites("%$key%")
    }

    /**
     * 保存在售列表缓存
     */
    @WorkerThread
    suspend fun savePurchasingCache(purchasing: List<PurchasingCache>) {
        databaseDao.deleteAllCachedPurchasing()
        databaseDao.savePurchasingCache(purchasing)
    }

    /**
     * 获取在售的缓存
     */
    @WorkerThread
    suspend fun getPurchasingCache(): List<PurchasingCache> {
        return databaseDao.getPurchasingCache()
    }
}
package com.example.schoolairdroprefactoredition.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.schoolairdroprefactoredition.database.pojo.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {

    /**
     * 获取未读消息数量
     * 包括网络和在线
     */
    @Query("select * from chat_offline_num_detail where receiver_id = :receiverID order by send_time desc")
    fun getChatList(receiverID: String): Flow<List<ChatOfflineNumDetail>>

    /**
     * 获取10条最新消息
     */
    @Query("select * from chat_history where (receiver_id = :receiverID and sender_id = :senderID) or (receiver_id = :senderID and sender_id = :receiverID) order by send_time desc limit 10")
    suspend fun getLatestChat(receiverID: String, senderID: String): List<ChatHistory>

    /**
     * 获取特定消息之后的10条消息
     */
    @Query("select * from chat_history where (receiver_id = :receiverID and sender_id = :senderID) or (receiver_id = :senderID and sender_id = :receiverID) and send_time < (select send_time from chat_history where fingerprint = :fp) order by send_time desc limit 10")
    suspend fun getChat(receiverID: String, senderID: String, fp: String): List<ChatHistory>

    /**
     * 查询本地用户信息缓存
     */
    @Query("select * from user_info where user_id = :userID")
    fun getUserInfo(userID: Int): List<UserCache>

    /**
     * 保存用户信息缓存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserInfo(userCache: UserCache)

    /**
     * 保存收到的离线消息数量
     *
     * 发送者接收者复合主键已存在则直接替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>)

    /**
     * 保存收到的实时消息的或者网络请求到的离线消息
     *
     * 指纹码冲突代表消息重复，直接丢弃即可
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveChat(histories: List<ChatHistory>)

    /**
     * 保存收到的实时消息的或者网络请求到的离线消息
     *
     * 指纹码冲突代表消息重复，直接丢弃即可
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveChat(history: ChatHistory)

    /**
     * ack未读消息数量，将来自sender的消息未读数置为0
     */
    @Query("update offline set unread_num = 0 where sender_id = :senderID and receiver_id = :receiverID")
    suspend fun ackOfflineNum(receiverID: String, senderID: String)

    /**
     * 更新来自某个用户的最后一条消息指纹以及是否还有离线消息标志
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastMessage(lastFromUserInformation: LastFromUserInformation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastMessage(lastFromUserInformation: List<LastFromUserInformation>)

    /**
     * 获取最后一条消息指纹和离线消息标志
     */
    @Query("select * from last_messages where user_id = :senderID")
    suspend fun getLastFromUserInformation(senderID: String): LastFromUserInformation?

}
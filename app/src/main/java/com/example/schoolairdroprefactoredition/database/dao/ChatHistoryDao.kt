package com.example.schoolairdroprefactoredition.database.dao

import androidx.room.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {

    /**
     * 获取未读消息数量
     * 包括网络和在线
     */
    @Query("select * from offline_num_detail where my_id = :receiverID order by send_time desc")
    fun getChatList(receiverID: String): Flow<List<ChatOfflineNumDetail>>

    /**
     * 获取默认数量条最新消息
     */
    @Query("select * from offline where (receiver_id = :receiverID and sender_id = :senderID) or (receiver_id = :senderID and sender_id = :receiverID) order by send_time desc limit " + ConstantUtil.DATA_FETCH_DEFAULT_SIZE)
    suspend fun getLatestChat(receiverID: String, senderID: String): List<ChatHistory>

    /**
     * 获取特定消息之后的默认数量条消息
     */
    @Query("select * from offline where (receiver_id = :receiverID and sender_id = :senderID or receiver_id = :senderID and sender_id = :receiverID) and send_time < (select send_time from offline where fingerprint = :fp) order by send_time desc limit " + ConstantUtil.DATA_FETCH_DEFAULT_SIZE)
    suspend fun getChat(receiverID: String, senderID: String, fp: String): List<ChatHistory>

    /**
     * 查询本地用户信息缓存
     */
    @Query("select * from user_info where user_id = :userID")
    suspend fun getUserCache(userID: Int): UserCache?

    /**
     * 保存用户信息缓存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserCache(userCache: UserCache)

    /**
     * 保存用户信息缓存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserCache(userCaches: List<UserCache>)

    /**
     * 更新和保存消息列表
     * 发送者接收者复合主键已存在则直接替换
     *
     * 获取离线消息数量时更新消息列表
     * 本方法可以设置会话可见性，另见[setDisplay]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>)

    /**
     * 插入消息列表
     *
     * 不要直接使用该方法，使用[saveOfflineNum]
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOfflineNum(offlineNum: ChatOfflineNum): Long

    /**
     * 更新消息列表
     *
     * 不要直接使用该方法，使用[saveOfflineNum]
     */
    @Query("update offline_num set unread_num = unread_num + 1, latest_fingerprint = :fingerprint, display = 1 where counterpart_id = :counterpartId and my_id = :myID")
    suspend fun updateOfflineNumFromCounterpart(counterpartId: String, myID: String, fingerprint: String)

    @Query("update offline_num set latest_fingerprint = :fingerprint, display = 1 where counterpart_id = :counterpartId and my_id = :myID")
    suspend fun updateOfflineNumFromMy(counterpartId: String, myID: String, fingerprint: String)

    @Transaction
    suspend fun saveOfflineNum(offlineNum: ChatOfflineNum, isSentFromCounterpart: Boolean) {
        val result = insertOfflineNum(offlineNum)
        if (result == -1L) {
            if (isSentFromCounterpart) {
                updateOfflineNumFromCounterpart(offlineNum.counterpart_id, offlineNum.my_id, offlineNum.latest_fingerprint)
            } else {
                updateOfflineNumFromMy(offlineNum.counterpart_id, offlineNum.my_id, offlineNum.latest_fingerprint)
            }
        }
    }

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
    @Query("update offline_num set unread_num = 0 where counterpart_id = :senderID and my_id = :receiverID")
    suspend fun ackOfflineNum(receiverID: String, senderID: String)

    /**
     * 更新来自某个用户的最后一条消息指纹以及是否还有离线消息标志
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastMessage(lastFromUserInformation: LastFromUserInformation)

    /**
     * 更新来自某个用户的最后一条消息指纹以及是否还有离线消息标志
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastMessage(lastFromUserInformation: List<LastFromUserInformation>)

    /**
     * 获取最后一条消息指纹和离线消息标志
     */
    @Query("select * from last_messages where user_id = :senderID")
    suspend fun getLastFromUserInformation(senderID: String): LastFromUserInformation?

    /**
     * 设置与某个用户的会话显示与隐藏，亦即将display置1与置0
     *
     * Ⅰ、具体到display何时置1，有两个时机
     * 1、[com.example.schoolairdroprefactoredition.viewmodel.MessageViewModel.saveOfflineNum]
     * 当上线时调用离线消息数量接口，直接获取到消息列表，此时将与这些用户的会话display都置为1。
     * 此情形由于需要处理其他数据，在viewmodel层便已顺便处理，无需在repository中处理
     * 2、[com.example.schoolairdroprefactoredition.repository.DatabaseRepository.saveHistory]参数非list
     * 当保存 自己发送的消息 和 回调接收的单条消息 时将会话display置为1。需要在repository中处理
     *
     * Ⅱ、置0只有一个时机
     * [com.example.schoolairdroprefactoredition.repository.DatabaseRepository.hideChannel]
     * 用户将会话侧滑删除之后置0
     *
     * Ⅲ、如何置0或置1
     * 1、[saveOfflineNum]将在上述具有消息被发送和接收时的情形改变display值
     * 2、而本方法将没有消息被收到或发送时的情形改变display值
     *
     * @param display 0 隐藏 1 显示
     */
    @Query("update offline_num set display = :display where counterpart_id = :counterpartId")
    suspend fun setDisplay(counterpartId: String, display: Int)
}
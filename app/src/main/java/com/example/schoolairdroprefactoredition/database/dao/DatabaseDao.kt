package com.example.schoolairdroprefactoredition.database.dao

import androidx.room.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.database.pojo.*
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    /**
     * 获取消息列表
     *
     * [com.example.schoolairdroprefactoredition.scene.main.messages.MessagesFragment]观察此数据变化
     * 当有来自其他人的消息被接收时或自己的消息发送时消息列表将会自动变化
     */
    @Query("select * from offline_num_detail where my_id = :receiverID order by send_time desc")
    fun getChatList(receiverID: String): Flow<List<ChatOfflineNumDetail>>

//    /**
//     * 获取与某人会话中的最新插入的消息，可能是来自对方的，也可能是我自己发送的
//     *
//     * 在每次接收到消息以及我自己发送消息的时候就更新一次
//     * todo 在room支持union时将or替换
//     */
//    @Query("select * from offline where (sender_id = :myID and receiver_id = :counterpartID or receiver_id = :myID and sender_id = :counterpartID) and send_time > :start order by send_time desc")
//    fun getOneLatestMessage(myID: String, counterpartID: String, start: Long): Flow<List<ChatHistory>>

    /**
     * 获取默认数量条最新消息
     *
     * 仅在打开聊天界面时调用一次
     */
    @Query("select * from offline where (receiver_id = :receiverID and sender_id = :senderID) or (receiver_id = :senderID and sender_id = :receiverID) order by send_time desc limit " + ConstantUtil.DEFAULT_PAGE_SIZE)
    suspend fun getLatestChat(receiverID: String, senderID: String): List<ChatHistory>

    /**
     * 获取特定时间戳之后的默认数量条消息
     *
     * 用于下拉获取更多的逻辑
     */
    @Query("select * from offline where (receiver_id = :receiverID and sender_id = :senderID or receiver_id = :senderID and sender_id = :receiverID) and send_time < :start order by send_time desc limit " + ConstantUtil.DEFAULT_PAGE_SIZE)
    suspend fun getChat(receiverID: String, senderID: String, start: Long): List<ChatHistory>

    /**
     * 查询本地用户信息缓存
     */
    @Query("select * from user_info where user_id = :userID limit 1")
    suspend fun getUserCache(userID: Int): UserCache?

    /**
     * 保存单个用户信息缓存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserCache(userCache: UserCache)

    /**
     * 当该用户信息未在本地被缓存时，需要将其id放入本地数据库，否则消息列表将不会更新，因为消息列表的视图依赖于用户信息
     *
     * 2021/2/22 Bug fix 此视图查询依赖于用户表，当对方是没有遇到过的用户，即本地没有该用户的缓存时，对方发来在线的第一条消息，将不会更新消息列表，因此所有发来消息的用户，尽管只有用户id，也要保存用户信息
     * 另见消息列表视图[ChatOfflineNumDetail]
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFirstUserCache(userCache: UserCache)

    /**
     * 批量保存用户信息缓存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserCache(userCaches: List<UserCache>)

    /**
     * 更新和保存消息列表
     * 发送者接收者复合主键已存在则直接替换
     *
     * 获取离线消息数量时更新消息列表
     * 本方法可以设置会话可见性，另见[setChannelDisplay]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOfflineNum(offlineNums: List<ChatOfflineNum>)

    /**
     * 更新消息列表，当消息列表中已经存在会话时将变为更新会话，否则将插入一条新的
     *
     * @param isSentFromCounterpart 这条消息是否是来自对方的
     */
    @Transaction
    suspend fun saveOfflineNum(offlineNum: ChatOfflineNum, isSentFromCounterpart: Boolean) {
        val result = insertOfflineNum(offlineNum)
        if (result == -1L) {
            if (isSentFromCounterpart) {
                // 消息来自对方
                updateOfflineNumFromCounterpart(offlineNum.counterpart_id, offlineNum.my_id, offlineNum.latest_fingerprint)
            } else {
                // 消息是我自己发出的
                updateOfflineNumFromMe(offlineNum.counterpart_id, offlineNum.my_id, offlineNum.latest_fingerprint)
            }
        }
    }

    /**
     * 插入消息列表
     *
     * 不要直接使用该方法，使用[saveOfflineNum]
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOfflineNum(offlineNum: ChatOfflineNum): Long

    /**
     * 收到来自对方的消息时更新消息列表
     *
     * 不要直接使用该方法，使用[saveOfflineNum]
     */
    @Query("update offline_num set unread_num = unread_num + 1, latest_fingerprint = :fingerprint, display = 1 where counterpart_id = :counterpartId and my_id = :myID")
    suspend fun updateOfflineNumFromCounterpart(counterpartId: String, myID: String, fingerprint: String)

    /**
     * 我自己发送消息时更新消息列表
     *
     * 不压迫直接使用该方法，使用[saveOfflineNum]
     */
    @Query("update offline_num set latest_fingerprint = :fingerprint, display = 1 where counterpart_id = :counterpartId and my_id = :myID")
    suspend fun updateOfflineNumFromMe(counterpartId: String, myID: String, fingerprint: String)

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
     * ack未读消息数量，将来自某个用户的消息未读数置为0，即在消息列表上展示的红色圈圈中的数字，若为0则不显示红圈
     */
    @Query("update offline_num set unread_num = 0 where counterpart_id = :senderID and my_id = :receiverID")
    suspend fun ackOfflineNum(receiverID: String, senderID: String)

    /**
     * 更新对于某个用户是否需要继续获取离线消息和ack
     *
     * 用于进入聊天页面时和聊天界面下拉加载更多的时候判断，若true则发起请求，否则仅需本地查询即可
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePullFlag(pullFlag: PullFlag)

    /**
     * 更新对于某个用户是否需要继续获取离线消息和ack
     *
     * 用于进入聊天页面时和聊天界面下拉加载更多的时候判断，若true则发起请求，否则仅需本地查询即可
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePullFlag(pullFlag: List<PullFlag>)

    /**
     * 获取对于某个用户是否需要继续拉取消息或ack
     *
     * 用于在聊天界面下拉加载更多的时候判断，若true则发起请求，否则仅需本地查询即可
     */
    @Query("select * from pull_flag where user_id = :senderID limit 1")
    suspend fun getPullFlag(senderID: String): PullFlag?

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
    @Query("update offline_num set display = :display where counterpart_id = :counterpartId and my_id = :myID ")
    suspend fun setChannelDisplay(myID: String, counterpartId: String, display: Int)

    /**
     * 添加物品收藏
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: Favorite)

    /**
     * 取消物品收藏
     */
    @Query("delete from favorite where goods_id = :goodsID")
    suspend fun removeFavorite(goodsID: Int)

    /**
     * 查询物品是否被收藏
     */
    @Query("select exists(select goods_id from favorite where goods_id = :goodsID limit 1)")
    suspend fun isFavorite(goodsID: Int): Boolean

    /**
     * 更新消息发送状态
     */
    @Query("update offline set status = :status where fingerprint = :fingerprint")
    suspend fun updateMessageStatus(fingerprint: String, @ChatRecyclerAdapter.MessageSendStatus status: Int)

    /**
     * 获取设备上收藏的所有物品
     */
    @Query("select * from favorite")
    suspend fun getFavorites(): List<Favorite>

    /**
     * 查询设备上收藏的物品
     */
    @Query("select * from favorite where goods_name like :key")
    suspend fun getFavorites(key: String): List<Favorite>

    /**
     * 保存进入app时第一次获取的淘物内容
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePurchasingCache(purchasing: List<PurchasingCache>)

    /**
     * 获取缓存的淘物信息
     */
    @Query("select * from purchasing")
    suspend fun getPurchasingCache(): List<PurchasingCache>

    /**
     * 在获取了新的物品信息之后删除之前的缓存
     */
    @Query("delete from purchasing")
    suspend fun deleteAllCachedPurchasing()
}
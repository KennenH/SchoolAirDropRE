package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.domain.DomainOfflineNum
import com.example.schoolairdroprefactoredition.domain.DomainResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface IMApi {

    /**
     * 获取离线消息数量
     */
    @POST("im/offline/num")
    fun getOfflineNum(@Header("Authorization") token: String): Call<DomainOfflineNum>

    /**
     * 获取来自特定用户的离线消息
     *
     * 当有临界消息指纹时代表二次拉取，否则表示进入页面时的首次拉取
     * 二次拉取可以带消息指纹列表用以ack离线消息
     * 被ack的消息以后将不再被发送
     * @param startTime 从这个临界时间开始找接下来n条数据，既然是从这条消息开始找，说明前端一定保存并处理了此消息
     * 之前的所有消息，所以该消息也作为ack的依据
     */
    @FormUrlEncoded
    @POST("im/offline/pull")
    fun getOffline(
            @Header("Authorization") token: String,
            @Field("sender_id") senderID: String,
            @Field("start") startTime: Long): Call<DomainOffline>

    /**
     * ack来自特定用户的离线消息
     *
     * @param startTime 从这个临界时间开始，迟于这个时间的所有消息都将被ack
     */
    @FormUrlEncoded
    @POST("im/offline/ack")
    fun ackOffline(
            @Header("Authorization") token: String,
            @Field("sender_id") senderID: String,
            @Field("start") startTime: Long)
}
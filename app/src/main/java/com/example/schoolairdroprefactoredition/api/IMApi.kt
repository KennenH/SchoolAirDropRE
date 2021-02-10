package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.domain.DomainOfflineNum
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
     * @param fingerprint 从这条消息开始找
     * @param ackList 需要ack的离线消息指纹列表
     */
    @FormUrlEncoded
    @POST("im/offline/pull")
    fun getOffline(@Header("Authorization") token: String,
                   @Field("sender_id") senderID: String,
                   @Field("start") fingerprint: String,
                   @Field("ack") ackList: List<String>?): Call<DomainOffline>

}
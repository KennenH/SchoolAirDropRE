package com.example.schoolairdroprefactoredition.utils

import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import net.x52im.mobileimsdk.server.protocal.Protocal
import kotlin.collections.ArrayList

object MessageUtil {

    /**
     * 将服务器的离线消息[DomainOffline.DataBean]转换为本地数据类型[ChatHistory]
     */
    @JvmStatic
    fun offlineToChatHistory(offline: List<DomainOffline.DataBean>): List<ChatHistory> {
        val histories = ArrayList<ChatHistory>()
        for (bean in offline) {
            histories.add(ChatHistory(
                    bean.finger_print,
                    bean.sender_id,
                    bean.receiver_id,
                    bean.message_type,
                    bean.message,
                    bean.send_time,
                    ChatRecyclerAdapter.MessageSendStatus.SUCCESS))
        }
        return histories
    }

    /**
     * 防止多次new新的字符串
     */
    private const val S_ = ""

    @JvmStatic
    fun createProtocol(fingerprints: List<String>): ArrayList<Protocal> {
        val list = ArrayList<Protocal>(fingerprints.size)
        for (fingerprint in fingerprints) {
            list.add(Protocal(0, S_, S_, S_, true, fingerprint))
        }
        return list
    }
}
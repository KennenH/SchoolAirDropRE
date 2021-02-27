package com.example.schoolairdroprefactoredition.utils

import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import net.x52im.mobileimsdk.server.protocal.Protocal
import kotlin.collections.ArrayList

class MessageUtil {
    companion object {
        /**
         * 将服务器的离线消息[DomainOffline.DataBean]转换为本地数据类型[ChatHistory]
         */
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
    }
}
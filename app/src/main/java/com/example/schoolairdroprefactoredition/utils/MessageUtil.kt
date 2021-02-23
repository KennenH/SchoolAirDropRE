package com.example.schoolairdroprefactoredition.utils

import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainOffline
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.util.*
import kotlin.collections.ArrayList

class MessageUtil {
    companion object {
        /**
         * 将[Protocal]转换为[ChatHistory]
         */
        fun protocalToChatHistory(protocal: Protocal): ChatHistory {
            return ChatHistory(protocal.fp,
                    protocal.from,
                    protocal.to,
                    protocal.typeu,
                    protocal.dataContent,
                    System.currentTimeMillis(),
                    0)
        }

        /**
         * 将服务器的离线消息[DomainOffline.DataBean]转换为本地数据类型[ChatHistory]
         */
        fun offlineToChatHistory(offline: List<DomainOffline.DataBean>): List<ChatHistory> {
            val histories = ArrayList<ChatHistory>()
            for (bean in offline) {
                histories.add(ChatHistory(
                        bean.fingerPrint,
                        bean.senderID,
                        bean.receiverID,
                        bean.messageType,
                        bean.message,
                        bean.sendTime,
                        0))
            }
            return histories
        }
    }
}
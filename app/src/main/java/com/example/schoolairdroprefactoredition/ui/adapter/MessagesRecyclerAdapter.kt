package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.database.pojo.ChatOfflineNumDetail
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.TimeUtil
import java.util.*

/**
 * 主页下消息列表子页面的Adapter
 */
class MessagesRecyclerAdapter : BaseQuickAdapter<ChatOfflineNumDetail, BaseViewHolder>(R.layout.item_home_messages) {

    /**
     * 获取第position个会话的对方id
     */
    fun getCounterpartIdAt(position: Int): String? {
        return if (data.size > position) {
            data[position]?.counterpart_id
        } else {
            null
        }
    }

    override fun convert(holder: BaseViewHolder, item: ChatOfflineNumDetail) {
        // 加载头像
        item.counterpart_avatar.let {
            if (it != null) {
                // 当头像url不为空时直接显示
                ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), ConstantUtil.QINIU_BASE_URL + it)
            } else {
                // 头像为空，先显示默认头像，等待外部将数据加载完毕
                ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), ConstantUtil.QINIU_BASE_URL + ConstantUtil.DEFAULT_AVATAR)
                // todo 想办法从外面获取用户信息
            }
        }

        // 显示名字
        item.counterpart_name.let {
            if (it != null) {
                // 当名字字段不为空时直接显示
                holder.setText(R.id.messages_user_name, it)
            } else {
                // 否则先显示用户id作为placeholder
                holder.setText(R.id.messages_user_name, item.counterpart_id)
                // todo 想办法从外面获取用户信息
            }
        }

        // 显示未读数量
        val unread = item.unread_num
        if (unread > 0) {
            holder.setGone(R.id.messages_unread_num, false)
            holder.setText(R.id.messages_unread_num, item.unread_num.toString())
        } else {
            holder.setGone(R.id.messages_unread_num, true)
        }

        // 显示时间
        holder.setText(R.id.messages_send_time, TimeUtil.getChatTimeSpanByNow(Date(item.send_time)))

        // 若消息类型为0则直接显示文本内容，若为1则为图片则仅显示为 [图片]
        if (item.message_type == 0 || item.message_type == -1) {
            holder.setText(R.id.messages_abstract, item.message)
        } else if (item.message_type == 1) {
            holder.setText(R.id.messages_abstract, context.getString(R.string.picture))
        }

        // 点击聊天，进入聊天界面
        holder.itemView.setOnClickListener {
            val counterpartInfo = DomainUserInfo.DataBean()
            counterpartInfo.userId = item.counterpart_id.toInt()
            counterpartInfo.userName = item.counterpart_name
            counterpartInfo.userAvatar = item.counterpart_avatar
            ChatActivity.start(context, counterpartInfo)
        }
    }
}
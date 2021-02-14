package com.example.schoolairdroprefactoredition.ui.adapter

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.database.pojo.ChatOfflineNumDetail
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.TimeUtil
import com.example.schoolairdroprefactoredition.viewmodel.MessageViewModel
import com.example.schoolairdroprefactoredition.viewmodel.UserViewModel

/**
 * 主页消息列表的Adapter
 */
class MessagesRecyclerAdapter(private val viewModel: MessageViewModel) : BaseQuickAdapter<ChatOfflineNumDetail, BaseViewHolder>(R.layout.item_home_messages) {

    /**
     * 获取第position个会话的对方id
     */
    fun getCounterpartIdAt(position: Int): String {
        return data[position].counterpart_id
    }

    override fun convert(holder: BaseViewHolder, item: ChatOfflineNumDetail) {
        // 头像显示，空则显示默认作为替代
        if (item.counterpart_avatar != null) {
            ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ConstantUtil.DEFAULT_AVATAR)
        } else {
            ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ImageUtil.fixUrl(item.counterpart_avatar))
        }

        // 用户名称显示，空则显示用户id作为替代
        if (item.counterpart_name == null) {
            holder.setText(R.id.messages_user_name, item.counterpart_id)
            // 若为空则获取用户信息
            val userInfo = viewModel.getUserBaseInfo(item.counterpart_id.toInt()).value
            userInfo?.let {
                LogUtils.d("已获取网络用户信息")
                ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + it.user_avatar)
                holder.setText(R.id.messages_user_name, it.user_id.toString())
            }
        } else {
            holder.setText(R.id.messages_user_name, item.counterpart_name)
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
        holder.setText(R.id.messages_send_time, TimeUtil.getChatTimeSpanByNow(item.send_time))

        // 若消息类型为0则直接显示文本内容，若为1则为图片则仅显示为 [图片]
        if (item.message_type == 0 || item.message_type == -1) {
            holder.setText(R.id.messages_abstract, item.message)
        } else if (item.message_type == 1) {
            holder.setText(R.id.messages_abstract, context.getString(R.string.picture))
        }

        // 点击聊天，进入聊天界面
        holder.itemView.setOnClickListener {
            val userInfo = DomainUserInfo.DataBean()
            userInfo.userId = item.counterpart_id.toInt()
            userInfo.userAvatar = item.counterpart_avatar
            userInfo.userName = item.counterpart_name
            ChatActivity.start(context, userInfo)
        }
    }
}
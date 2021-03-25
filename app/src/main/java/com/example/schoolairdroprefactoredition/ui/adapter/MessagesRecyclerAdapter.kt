package com.example.schoolairdroprefactoredition.ui.adapter

import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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
import java.util.*

/**
 * 主页下消息列表子页面的Adapter
 */
class MessagesRecyclerAdapter : BaseQuickAdapter<ChatOfflineNumDetail, BaseViewHolder>(R.layout.item_home_messages) {

    private var mUserInfoRequestListener: UserInfoRequestListener? = null

    /**
     * 用户信息请求
     */
    interface UserInfoRequestListener {
        /**
         * 当adapter中的用户信息不足时请求外部获取
         */
        fun onUserInfoRequest(userID: String, myID: String)
    }

    fun setUserInfoRequestListener(listener: UserInfoRequestListener) {
        mUserInfoRequestListener = listener
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
                mUserInfoRequestListener?.onUserInfoRequest(item.counterpart_id, item.my_id)
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

        // 获取消息显示视图
        val messageAbstract = holder.itemView.findViewById<TextView>(R.id.messages_abstract)
        // 若消息类型为0（或者-1，为测试消息;或2，提示消息）则直接显示文本内容，若为1则为图片则仅显示为 [图片]
        when (item.message_type) {
            ConstantUtil.MESSAGE_TYPE_TEXT, -1, ConstantUtil.MESSAGE_TYPE_TIP -> {
                messageAbstract.text = item.message
            }
            ConstantUtil.MESSAGE_TYPE_IMAGE -> {
                messageAbstract.text = context.getString(R.string.picture)
            }
        }

        when (item.status) {
            // 正在发送状态
            ChatRecyclerAdapter.MessageSendStatus.SENDING -> {
                messageAbstract.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_sending,
                        0, 0, 0)
            }
            // 发送成功状态
            ChatRecyclerAdapter.MessageSendStatus.SUCCESS -> {
                messageAbstract.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            // 发送失败状态
            ChatRecyclerAdapter.MessageSendStatus.FAILED -> {
                messageAbstract.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_attention_small_red,
                        0, 0, 0)
            }
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